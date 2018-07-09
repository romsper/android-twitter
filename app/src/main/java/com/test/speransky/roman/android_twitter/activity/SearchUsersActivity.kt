package com.test.speransky.roman.android_twitter.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.test.speransky.roman.android_twitter.R
import com.test.speransky.roman.android_twitter.adapter.UsersAdapter
import com.test.speransky.roman.android_twitter.pojo.User
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import android.widget.TextView



class SearchUsersActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var queryEditText: EditText
    private lateinit var searchButton: Button

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)
        initRecyclerView()

        toolbar = findViewById(R.id.toolbar)
        queryEditText = toolbar.findViewById(R.id.query_edit_text)
        searchButton = toolbar.findViewById(R.id.search_button)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        searchButton.setOnClickListener { searchUsers() }

        queryEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUsers()
                return@OnEditorActionListener true
            }
            false })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        usersRecyclerView = findViewById(R.id.users_recycler_view)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        val onUserClickListener = object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = Intent(this@SearchUsersActivity, UserInfoActivity::class.java)
                intent.putExtra(UserInfoActivity().USER_ID, user.id)
                startActivity(intent)
            }
        }

        usersAdapter = UsersAdapter(onUserClickListener)
        usersRecyclerView.adapter = usersAdapter
    }

    private fun searchUsers() {
        val users = getUsers()
        usersAdapter.clearItems()
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