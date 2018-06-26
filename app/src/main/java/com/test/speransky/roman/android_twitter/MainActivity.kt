package com.test.speransky.roman.android_twitter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var userImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userImageView = findViewById(R.id.user_image_view)

        Picasso
                .with(this)
                .load("http://i.imgur.com/DvpvklR.png")
                .into(userImageView)
    }
}
