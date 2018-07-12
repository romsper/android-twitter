package com.test.speransky.roman.android_twitter.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
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

class UserInfoActivity : AppCompatActivity() {
    val USER_ID: String = "userId"

    lateinit var httpClient: HttpClient

    lateinit var toolBar: Toolbar

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

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)

        val userId: Long = intent.getLongExtra(USER_ID, -1)
        Toast.makeText(this, "UserId - $userId", Toast.LENGTH_SHORT).show()

        userImageView = findViewById(R.id.user_image_view)
        nameTextView = findViewById(R.id.user_name_text_view)
        nickTextView = findViewById(R.id.user_nick_text_view)
        descriptionTextView = findViewById(R.id.user_description_text_view)
        locationTextView = findViewById(R.id.user_location_text_view)
        followingCountTextView = findViewById(R.id.following_count_text_view)
        followersCountTextView = findViewById(R.id.followers_count_text_view)

        initRecyclerView()

        httpClient = HttpClient()
        loadUserInfo(userId)

        loadTweets()
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

    private fun displayUserInfo(user: User) {
        supportActionBar?.title = user.name

        Picasso.get().load(user.imageUrl).into(userImageView)
        nameTextView.text = user.name
        nickTextView.text = user.nick
        descriptionTextView.text = user.description
        locationTextView.text = user.location
        followingCountTextView.text = user.followingCount.toString()
        followersCountTextView.text = user.followersCount.toString()
    }

    private fun getUser() = User(
            id = 1L,
            imageUrl = "http://i.imgur.com/DvpvklR.png",
            name = "DevColibri",
            nick = "@devcolibri",
            description = "Sample description",
            location = "USA",
            followingCount = 42,
            followersCount = 42
    )

    private fun loadUserInfo(userId: Long) {
        val readUserRunnable = Runnable {
            try {
                val userInfo = httpClient.readUserInfo(userId)
                Log.d("HttpTest", userInfo)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        Thread(readUserRunnable).start()
    }

    private fun initRecyclerView() {
        tweetsRecyclerView = findViewById(R.id.tweets_recycler_view)
        tweetsRecyclerView.layoutManager = LinearLayoutManager(this)

        tweetAdapter = TweetAdapter()
        tweetsRecyclerView.adapter = tweetAdapter
    }

    private fun loadTweets() {
        val tweets = getTweets()
        tweetAdapter.setItems(tweets)
    }

    private fun getTweets(): Collection<Tweet> {
        return asList(
                Tweet(getUser(), 1L, "Thu Dec 13 07:31:08 +0000 2017", "Очень длинное описание твита 1",
                        4L, 4L, "https://www.w3schools.com/w3css/img_fjords.jpg"),

                Tweet(getUser(), 2L, "Thu Dec 12 07:31:08 +0000 2017", "Очень длинное описание твита 2",
                        5L, 5L, "https://www.w3schools.com/w3images/lights.jpg"),

                Tweet(getUser(), 3L, "Thu Dec 11 07:31:08 +0000 2017", "Очень длинное описание твита 3",
                        6L, 6L, "https://www.w3schools.com/css/img_mountains.jpg")
        )
    }
}
