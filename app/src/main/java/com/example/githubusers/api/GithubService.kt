package com.example.githubusers.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/users") // We can use this same function to implement infinite scroll with pagination if we want
    fun searchUsers(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int): Call<GithubResponse>

}