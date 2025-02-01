package com.example.chatsapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.chatsapplication.R
import com.example.chatsapplication.databinding.ItemUserBinding
import com.example.chatsapplication.model.User

class UserAdapter(private var context: Context, private val dataSet: ArrayList<User>, var onClickListener:listener) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    companion object{
        const val UID="id"
    }
   inner class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(context)
                .load(user.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_send)
                .into(binding.userImage)
            binding.userName.text=user.name

            binding.root.setOnClickListener(){
                var position=adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    var item=dataSet[position]
                    onClickListener.onClick(item)
                }
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var binding=ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
        var user=dataSet[position]
        viewHolder.bind(user)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    interface listener{
        fun onClick(user: User)
    }

}
