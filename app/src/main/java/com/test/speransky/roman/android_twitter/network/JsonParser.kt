package com.test.speransky.roman.android_twitter.network

import com.google.gson.Gson
import com.test.speransky.roman.android_twitter.pojo.User

class JsonParser {
    private val gson = Gson()

    fun getUser(response: String): User {
        return gson.fromJson(response, User::class.java)
    }
}