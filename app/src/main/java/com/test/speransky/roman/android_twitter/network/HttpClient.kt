package com.test.speransky.roman.android_twitter.network

import java.net.HttpURLConnection
import java.net.URL
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants.HEADER_AUTHORIZATION
import java.io.InputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class HttpClient {
    private val GET = "GET"

    @Throws(IOException::class)
    fun readUserInfo(userId: Long): String {
        var requestUrl = "https://api.twitter.com/1.1/users/show.json?user_id=$userId"

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