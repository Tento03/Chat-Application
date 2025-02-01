package com.example.chatsapplication.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatsapplication.adapter.UserAdapter
import com.example.chatsapplication.databinding.ActivityLoginBinding
import com.example.chatsapplication.databinding.ActivityUserBinding
import com.example.chatsapplication.model.User
import com.example.chatsapplication.reglog.LoginActivity
import com.example.chatsapplication.reglog.ResetPasswordActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    companion object{
        const val ID="ID"
    }
    lateinit var binding: ActivityUserBinding
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    private val userList= arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgProfile.setOnClickListener(){
            Intent(this,ProfileActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.imgBack.setOnClickListener(){
            onBackPressed()
        }
        binding.logout.setOnClickListener(){
            var firebaseAuth=FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            Intent(this,LoginActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }

        recyclerView=binding.userRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(this)
        userAdapter=UserAdapter(this,userList,object :UserAdapter.listener{
            override fun onClick(user: User) {
                var intent=Intent(this@UserActivity,ChatActivity::class.java)
                intent.putExtra(ID,user.id)
                startActivity(intent)
            }

        })
        recyclerView.adapter=userAdapter
        var firebaseAuth=FirebaseAuth.getInstance()
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
        firebaseDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    var user=i.getValue(User::class.java)
                    if (user!=null && user.id!= firebaseAuth.currentUser!!.uid){
                        userList.add(user)
                    }
                    userAdapter.notifyDataSetChanged()
                }
                for (i in snapshot.children){
                    var user=i.getValue(User::class.java)
                    if (user!=null && user.id== firebaseAuth.currentUser!!.uid){
                        Glide.with(this@UserActivity)
                            .load(user.image)
                            .into(binding.imgProfile)
                        binding.idProfile.text=user.name
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserActivity, "${error.message}", Toast.LENGTH_SHORT)
            }

        })
    }
}