package com.example.chatsapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapplication.databinding.ItemLeftBinding
import com.example.chatsapplication.databinding.ItemRightBinding
import com.example.chatsapplication.model.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val dataSet: ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MESSAGE_TYPE_RIGHT=1
    private val MESSAGE_TYPE_LEFT=0

    inner class LeftViewHolder(var binding: ItemLeftBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chat: Chat){
            binding.tvMessage.text=chat.message
        }
    }
    inner class RightViewHolder(var binding: ItemRightBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chat: Chat){
            binding.tvMessage.text=chat.message
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Create a new view, which defines the UI of the list item
        return when(viewType){
            MESSAGE_TYPE_RIGHT->{
                var binding=ItemRightBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
                return RightViewHolder(binding)
            }

            else -> {
                var binding=ItemLeftBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
                return LeftViewHolder(binding)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when(viewHolder){
            is RightViewHolder->{
                viewHolder.bind(dataSet[position])
            }
            is LeftViewHolder->{
                viewHolder.bind(dataSet[position])
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        var firebaseAuth=FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser!!.uid == dataSet[position].senderId){
            return MESSAGE_TYPE_RIGHT
        }
        else{
            return MESSAGE_TYPE_LEFT
        }
    }
}