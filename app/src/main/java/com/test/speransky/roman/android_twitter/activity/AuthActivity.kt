package com.test.speransky.roman.android_twitter.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.test.speransky.roman.android_twitter.R
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class AuthActivity : AppCompatActivity() {
    lateinit var twitterLoginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        twitterLoginButton = findViewById(R.id.login_button)

        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                val userId: Long = result.data.userId
                navigateToUserInfo(userId)
            }

            override fun failure(exception: TwitterException) {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToUserInfo(userId: Long) {
        val intent = Intent(this@AuthActivity, UserInfoActivity::class.java)
        intent.putExtra(UserInfoActivity().USER_ID, userId)
        startActivity(intent)
    }
}