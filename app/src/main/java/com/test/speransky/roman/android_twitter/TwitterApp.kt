package com.test.speransky.roman.android_twitter

import android.app.Application
import com.twitter.sdk.android.core.Twitter

class TwitterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Twitter.initialize(this)
    }
}