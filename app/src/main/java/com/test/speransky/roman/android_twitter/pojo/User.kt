package com.test.speransky.roman.android_twitter.pojo
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Long?,
    @SerializedName("name") var name: String?,
    @SerializedName("screen_name") var screenName: String?,
    @SerializedName("location") var location: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("profile_image_url") var profileImageUrl: String?,
    @SerializedName("followers_count") var followersCount: Int?,
    @SerializedName("favourites_count") var favouritesCount: Int?
)