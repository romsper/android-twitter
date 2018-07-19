package com.test.speransky.roman.android_twitter.network

import com.test.speransky.roman.android_twitter.pojo.Tweet
import com.test.speransky.roman.android_twitter.pojo.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject




class JsonParser {

    @Throws(JSONException::class)
    fun getUser(response: String): User {
        val userJson = JSONObject(response)
        return getUser(userJson)
    }

    @Throws(JSONException::class)
    private fun getUser(userJson: JSONObject): User {
        val id = userJson.getLong("id")
        val name = userJson.getString("name")
        val nick = userJson.getString("screen_name")
        val location = userJson.getString("location")
        val description = userJson.getString("description")
        val imageUrl = userJson.getString("profile_image_url")
        val followersCount = userJson.getInt("followers_count")
        val followingCount = userJson.getInt("favourites_count")

        return User(id = id, profileImageUrl = imageUrl, name = name, screenName = nick, description = description, location = location, followersCount = followingCount, favouritesCount = followersCount)
    }

    @Throws(JSONException::class)
    fun getTweets(response: String): Collection<Tweet> {
        val jsonArray = JSONArray(response)
        val tweetsResult = ArrayList<Tweet>()

        for (i in 0 until jsonArray.length()) {
            val tweetJson = jsonArray.getJSONObject(i)
            val id = tweetJson.getLong("id")
            val creationDate = tweetJson.getString("created_at")
            val fullText = tweetJson.getString("full_text")
            val retweetCount = tweetJson.getLong("retweet_count")
            val likesCount = tweetJson.getLong("favorite_count")

            val imageUrl = getTweetImageUrl(tweetJson)

            val userJson = tweetJson.getJSONObject("user")
            val user = getUser(userJson)

            val tweet = Tweet(user, id, creationDate, fullText, retweetCount, likesCount, imageUrl)
            tweetsResult.add(tweet)
        }

        return tweetsResult
    }

    @Throws(JSONException::class)
    private fun getTweetImageUrl(tweetJson: JSONObject): String? {
        val entities = tweetJson.getJSONObject("entities")
        val mediaArray = if (entities.has("media")) entities.getJSONArray("media") else null
        val firstMedia = mediaArray?.getJSONObject(0)
        return firstMedia?.getString("media_url")
    }
}