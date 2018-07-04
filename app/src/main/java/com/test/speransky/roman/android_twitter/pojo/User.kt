package com.test.speransky.roman.android_twitter.pojo

data class User(
        var id: Long,
        var imageUrl: String?,
        var name: String,
        var nick: String,
        var description: String?,
        var location: String?,
        var followingCount: Int?,
        var followersCount: Int?
)