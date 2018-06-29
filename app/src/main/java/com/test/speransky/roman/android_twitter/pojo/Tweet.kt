package com.test.speransky.roman.android_twitter.pojo

data class Tweet(
        var user: User,
        var id: Long,
        var creationDate: String,
        var text: String,
        var retweetCount: Long,
        var favoriteCount: Long,
        var imageUrl: String?
)