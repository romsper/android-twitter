package com.test.speransky.roman.android_twitter.pojo
import com.google.gson.annotations.SerializedName


data class Tweet(
        var user: User,
        var id: Long,
        var creationDate: String,
        var text: String,
        var retweetCount: Long,
        var favoriteCount: Long,
        var imageUrl: String?
)


data class TweetNew(
    @SerializedName("created_at") var createdAt: String?,
    @SerializedName("id") var id: Long?,
    @SerializedName("full_text") var fullText: String?,
    @SerializedName("retweet_count") var retweetCount: Int?,
    @SerializedName("favorite_count") var favoriteCount: Int?,
    @SerializedName("entities") var entities: Entities?,
    @SerializedName("user") var user: User?
)

data class Entities(
    @SerializedName("media") var media: List<Media?>?
)

data class Media(
    @SerializedName("media_url") var mediaUrl: String?
)