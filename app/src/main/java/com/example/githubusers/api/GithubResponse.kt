package com.example.githubusers.api

import com.google.gson.annotations.SerializedName

class GithubResponse {

    @SerializedName("items")
    var users: List<GithubUser> = ArrayList()

}