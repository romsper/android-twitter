package com.test.speransky.roman.android_twitter.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.test.speransky.roman.android_twitter.R
import com.test.speransky.roman.android_twitter.adapter.UsersAdapter
import com.test.speransky.roman.android_twitter.pojo.User
import android.content.Intent

class SearchUsersActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)

        initRecyclerView()
        searchUsers()
    }

    private fun initRecyclerView() {
        usersRecyclerView = findViewById(R.id.users_recycler_view)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        val onUserClickListener = object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = Intent(this@SearchUsersActivity, MainActivity::class.java)
                intent.putExtra(MainActivity().USER_ID, user.id)
                startActivity(intent)
            }
        }

        usersAdapter = UsersAdapter(onUserClickListener)
        usersRecyclerView.adapter = usersAdapter
    }

    private fun searchUsers() {
        val users = getUsers()
        usersAdapter.setItems(users)
    }

    private fun getUsers() : Collection<User> {
        return listOf(
                User(
                        id = 1L,
                        imageUrl = "http://i.imgur.com/DvpvklR.png",
                        name = "DevColibri",
                        nick = "@devcolibri",
                        description = "Sample description",
                        location = "USA",
                        followingCount = 42,
                        followersCount = 42
                ),
                User(
                        id = 44196397L,
                        imageUrl = "https://pbs.twimg.com/profile_images/782474226020200448/zDo-gAo0_400x400.jpg",
                        name = "Elon Musk",
                        nick = "@elonmusk",
                        description = "Hat Salesman",
                        location = "Boring",
                        followingCount = 14,
                        followersCount = 13
                )
        )
    }
}