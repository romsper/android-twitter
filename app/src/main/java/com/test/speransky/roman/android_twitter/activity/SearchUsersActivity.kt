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
import com.test.speransky.roman.android_twitter.network.HttpClient
import android.os.AsyncTask
import android.annotation.SuppressLint
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import org.json.JSONException
import java.io.IOException


class SearchUsersActivity : AppCompatActivity() {
    lateinit var httpClient: HttpClient

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

        httpClient = HttpClient()

        searchButton.setOnClickListener { searchUsers() }

        queryEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUsers()
                return@OnEditorActionListener true
            }
            false })

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            searchUsers()
        }
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
        usersRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

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

    @SuppressLint("StaticFieldLeak")
    private fun searchUsers() {
        val query = queryEditText.text.toString()
        if(query.isEmpty()) {
            Toast.makeText(this@SearchUsersActivity, R.string.not_enough_symbols_msg, Toast.LENGTH_SHORT).show()
            return
        }

        UsersAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UsersAsyncTask : AsyncTask<String, Int, Collection<User>>() {

        override fun onPreExecute() {
            swipeRefreshLayout.isRefreshing = true

        }
        override fun doInBackground(vararg params: String): Collection<User>? {
            val query = params[0]
            return try {
                httpClient.readUsers(query)
            } catch (e: IOException) {
                null
            } catch (e: JSONException) {
                null
            }

        }

        override fun onPostExecute(users: Collection<User>) {
            swipeRefreshLayout.isRefreshing = false

            usersAdapter.clearItems()
            usersAdapter.setItems(users)
        }
    }
}