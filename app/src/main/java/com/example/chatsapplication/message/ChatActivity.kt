package com.example.chatsapplication.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatsapplication.R
import com.example.chatsapplication.adapter.ChatAdapter
import com.example.chatsapplication.databinding.ActivityChatBinding
import com.example.chatsapplication.model.Chat
import com.example.chatsapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter
    private val chatList= arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView=binding.chatRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(this)
        chatAdapter= ChatAdapter(chatList)
        recyclerView.adapter=chatAdapter

//        createNotificationChannel()
        var idReceiver=intent.getStringExtra(UserActivity.ID)
        var firebaseAuth=FirebaseAuth.getInstance()
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
        firebaseDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    var user=i.getValue(User::class.java)
                    if (user!=null && user.id==idReceiver){
                        Glide.with(this@ChatActivity)
                            .load(user.image)
                            .into(binding.imgProfile)
                        binding.tvUserName.text=user.name
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        readMessage()
        binding.btnSendMessage.setOnClickListener(){
            var chat=binding.etMessage.text.toString()
            var idSender= firebaseAuth.currentUser!!.uid
            if (chat.isNotEmpty() && idReceiver!=null && idSender!=null){
                sendMessage(idSender,idReceiver,chat)
                binding.etMessage.text.clear()

                val intent=Intent(this,UserActivity::class.java).also {
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE)
                val notificationBuilder=NotificationCompat.Builder(this,NotificationReceiver.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_send)
                    .setContentTitle("Firebase Chats Notification")
                    .setContentText(chat)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                with(NotificationManagerCompat.from(this)){
                    notify(NotificationReceiver.NOTIFICATION_ID,notificationBuilder.build())
                }
            }
            readMessage()
        }
    }

    private fun sendMessage(idSender: String, idReceiver: String, chatText: String) {
        val chatRef = FirebaseDatabase.getInstance().getReference("chat")
        val timestamp = System.currentTimeMillis()
        val chat = Chat(idSender, idReceiver, chatText, timestamp)
        chatRef.push().setValue(chat)
    }

    private fun readMessage() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val idReceiver = intent.getStringExtra(UserActivity.ID)
        val chatRef = FirebaseDatabase.getInstance().getReference("chat")

        chatRef.orderByChild("timestamp").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat != null &&
                        ((chat.senderId == firebaseAuth.currentUser?.uid && chat.receiverId == idReceiver) ||
                                (chat.senderId == idReceiver && chat.receiverId == firebaseAuth.currentUser?.uid))) {
                        chatList.add(chat)
                    }
                }
                chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error jika diperlukan
            }
        })
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name="notif"
            val desc="desc"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel(NotificationReceiver.CHANNEL_ID,name,importance).apply {
                description=desc
            }
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

}