package com.test.speransky.roman.android_twitter.pojo
import com.google.gson.annotations.SerializedName


data class Tweet(
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("id") var id: Long?,
        @SerializedName("full_text") var fullText: String?,
        @SerializedName("retweet_count") var retweetCount: Int?,
        @SerializedName("favorite_count") var favoriteCount: Int?,
        @SerializedName("user") var user: User?,
        var imageUrl: String?
)

