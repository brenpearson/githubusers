package com.example.githubusers.databinding

import com.example.githubusers.api.GithubUser

interface MainActivityListener {
    fun onUserClick(user: GithubUser)
}