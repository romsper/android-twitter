package com.test.speransky.roman.android_twitter.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.test.speransky.roman.android_twitter.R
import com.test.speransky.roman.android_twitter.pojo.User

class UsersAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val userList = ArrayList<User>()
    private lateinit var onUserClickListener: OnUserClickListener

    init {
            this.onUserClickListener = onUserClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.user_item_view, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder!!.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    fun setItems(users: Collection<User>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun clearItems() {
        userList.clear()
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.profile_image_view)
        private val nameTextView: TextView = itemView.findViewById(R.id.user_name_text_view)
        private val nickTextView: TextView = itemView.findViewById(R.id.user_nick_text_view)

        init {
            itemView.setOnClickListener {
                val user = userList[layoutPosition]
                onUserClickListener.onUserClick(user)
            }
        }

        fun bind(user: User) {
            Picasso.get().load(user.imageUrl).into(userImageView)
            nameTextView.text = user.name
            nickTextView.text = user.nick
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}