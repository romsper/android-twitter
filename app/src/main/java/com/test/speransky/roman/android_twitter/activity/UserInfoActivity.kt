package com.test.speransky.roman.android_twitter.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.test.speransky.roman.android_twitter.R
import com.test.speransky.roman.android_twitter.adapter.TweetAdapter
import com.test.speransky.roman.android_twitter.network.HttpClient
import com.test.speransky.roman.android_twitter.pojo.User
import com.test.speransky.roman.android_twitter.pojo.Tweet
import java.io.IOException
import java.util.Arrays.asList
import android.os.AsyncTask
import android.annotation.SuppressLint
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DividerItemDecoration
import org.json.JSONException
import android.support.v4.widget.SwipeRefreshLayout




class UserInfoActivity : AppCompatActivity() {
    val USER_ID: String = "userId"
    var taskInProgressCount = 0

    lateinit var httpClient: HttpClient

    lateinit var toolBar: Toolbar

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var userImageView: ImageView
    lateinit var nameTextView: TextView
    lateinit var nickTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var locationTextView: TextView
    lateinit var followingCountTextView: TextView
    lateinit var followersCountTextView: TextView

    lateinit var tweetsRecyclerView: RecyclerView
    lateinit var tweetAdapter: TweetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId: Long = intent.getLongExtra(USER_ID, -1);
        Toast.makeText(this, "UserId - $userId", Toast.LENGTH_SHORT).show()

        userImageView = findViewById(R.id.user_image_view)
        nameTextView = findViewById(R.id.user_name_text_view)
        nickTextView = findViewById(R.id.user_nick_text_view)
        descriptionTextView = findViewById(R.id.user_description_text_view)
        locationTextView = findViewById(R.id.user_location_text_view)
        followingCountTextView = findViewById(R.id.following_count_text_view)
        followersCountTextView = findViewById(R.id.followers_count_text_view)

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        httpClient = HttpClient()

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            tweetAdapter.clearItems()
            loadUserInfo(userId)
            loadTweets(userId)
        }

        initRecyclerView()
        loadUserInfo(userId)
        loadTweets(userId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val intent = Intent(this, SearchUsersActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    private fun initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view)

        ViewCompat.setNestedScrollingEnabled(tweetsRecyclerView, false)
        tweetsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        tweetsRecyclerView.layoutManager = LinearLayoutManager(this)
        tweetAdapter = TweetAdapter()
        tweetsRecyclerView.adapter = tweetAdapter
    }

    private fun loadUserInfo(userId: Long) {
        UserInfoAsyncTask().execute(userId)
    }

    private fun loadTweets(userId: Long) {
        TweetsAsyncTask().execute(userId)
    }

    private fun setRefreshLayoutVisible(visible: Boolean) {
        if (visible) {
            taskInProgressCount++
            if (taskInProgressCount == 1) swipeRefreshLayout.isRefreshing = true
        } else {
            taskInProgressCount--
            if (taskInProgressCount == 0) swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class TweetsAsyncTask : AsyncTask<Long, Int, Collection<Tweet>>() {

        override fun onPreExecute() {
            setRefreshLayoutVisible(true)
        }

         override fun doInBackground(vararg p0: Long?): Collection<Tweet>? {
             return try {
                 val userId = p0[0]
                 httpClient.readTweets(userId!!)

             } catch (e: IOException) {
                 e.printStackTrace()
                 null
             } catch (e: JSONException) {
                 e.printStackTrace()
                 null
             }

        }

        override fun onPostExecute(tweets: Collection<Tweet>) {
            setRefreshLayoutVisible(false)
            tweetAdapter.setItems(tweets)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UserInfoAsyncTask : AsyncTask<Long, Int, User?>() {

        override fun onPreExecute() {
            setRefreshLayoutVisible(true)
        }

        override fun doInBackground(vararg p0: Long?): User? {
            return try {
                val userId = p0[0]
                httpClient.readUserInfo(userId)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: JSONException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(user: User?) {
            setRefreshLayoutVisible(false)

            if(user != null) {
                displayUserInfo(user)
            }
            else {
                Toast.makeText(this@UserInfoActivity, R.string.loading_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserInfo(user: User) {
        supportActionBar?.title = user.name

        Picasso.get().load(user.profileImageUrl).into(userImageView)
        nameTextView.text = user.name
        nickTextView.text = "@${user.screenName}"
        descriptionTextView.text = user.description
        locationTextView.text = user.location
        followingCountTextView.text = user.favouritesCount.toString()
        followersCountTextView.text = user.followersCount.toString()
    }
}
