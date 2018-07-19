package com.test.speransky.roman.android_twitter.network

import com.test.speransky.roman.android_twitter.pojo.Tweet
import com.test.speransky.roman.android_twitter.pojo.User
import java.net.HttpURLConnection
import java.net.URL
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants.HEADER_AUTHORIZATION
import org.json.JSONException
import java.io.InputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class HttpClient {
    private val GET = "GET"
    private val jsonParser = JsonParser()

    private val EXTENDED_MODE = "&tweet_mode=extended"

    @Throws(IOException::class, JSONException::class)
    fun readTweets(userId: Long): Collection<Tweet> {
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=$userId$EXTENDED_MODE"
        val response = getResponse(requestUrl)
        return jsonParser.getTweets(response)
    }

    @Throws(IOException::class, JSONException::class)
    fun readUserInfo(userId: Long): User {
        val requestUrl = "https://api.twitter.com/1.1/users/show.json?user_id=$userId"
        val response = getResponse(requestUrl)
        return jsonParser.getUser(response)
    }

    fun readUsers(query: String): Collection<User> {
        val requestUrl = "https://api.twitter.com/1.1/users/search.json?q=$query"
        val encodedUrl = requestUrl.replace(" ", "%20")
        val response = getResponse(encodedUrl)
        return jsonParser.getUsers(response)
    }

    @Throws(IOException::class)
    private fun getResponse(requestUrl: String): String {
        val url = URL(requestUrl)
        val connection = url.openConnection() as HttpURLConnection

        val authHeader = getAuthHeader(requestUrl)
        connection.setRequestProperty(HEADER_AUTHORIZATION, authHeader)

        connection.connect()

        val input: InputStream
        val status = connection.responseCode
        input = if (status != HttpURLConnection.HTTP_OK) {
            connection.errorStream
        } else {
            connection.inputStream
        }

        return convertStreamToString(input)
    }

    @Throws(IOException::class)
    private fun convertStreamToString(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()
        var line = reader.readLine()

        when(line != null) {
            true -> { sb.append(line).append("\n"); }
            else -> stream.close()
        }
        return sb.toString()
    }


    private fun getAuthHeader(url: String): String {
        val authConfig = TwitterCore.getInstance().authConfig
        val session = TwitterCore.getInstance().sessionManager.activeSession

        return OAuth1aHeaders().getAuthorizationHeader(authConfig,
                session.authToken, null, GET, url, null)
    }
}