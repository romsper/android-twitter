package com.test.speransky.roman.android_twitter.network

import com.google.gson.GsonBuilder
import com.test.speransky.roman.android_twitter.pojo.Tweet
import com.test.speransky.roman.android_twitter.pojo.User
import com.google.gson.reflect.TypeToken

class JsonParser {
    private val gson = GsonBuilder()
            .registerTypeAdapter(Tweet::class.java, TweetDeserializer())
            .create()

    //---USER---
    fun getUser(response: String): User {
        return gson.fromJson(response, User::class.java)
    }

    //---USERS---
    fun getUsers(response: String): Collection<User> {
        val usersType = object : TypeToken<Collection<User>>() {}.type
        return gson.fromJson(response, usersType)
    }

    //--TWEETS---
    fun getTweets(response: String): Collection<Tweet> {
        val tweetsType = object : TypeToken<Collection<Tweet>>() {}.type
        return gson.fromJson(response, tweetsType)
    }
}