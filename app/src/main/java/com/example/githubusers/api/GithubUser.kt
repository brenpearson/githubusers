package com.example.githubusers.api

import com.google.gson.annotations.SerializedName

class GithubUser {

    @SerializedName("login")
    var username: String? = null

    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    @SerializedName("html_url")
    var profileUrl: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("score")
    var accuracy: Float? = null


}