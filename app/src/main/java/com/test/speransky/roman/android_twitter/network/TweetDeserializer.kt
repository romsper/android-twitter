package com.test.speransky.roman.android_twitter.network

import com.google.gson.*
import com.test.speransky.roman.android_twitter.pojo.Tweet
import java.lang.reflect.Type


class TweetDeserializer : JsonDeserializer<Tweet> {
    private val gson = Gson()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Tweet {
        val tweetJson = json.asJsonObject
        return gson.fromJson(tweetJson, Tweet::class.java)
                .apply { this.imageUrl = getTweetImageUrl(tweetJson) }
    }

    private fun getTweetImageUrl(tweetJson: JsonObject): String? {
        val entities = tweetJson.get("entities").asJsonObject
        val mediaArray = if (entities.has("media")) entities.get("media").asJsonArray else null
        val firstMedia = mediaArray?.get(0)?.asJsonObject
        return firstMedia?.get("media_url")?.asString
    }
}