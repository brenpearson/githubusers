package com.example.githubusers.databinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.api.GithubUser

class MainActivityViewModel : ViewModel() {
    val searchText = MutableLiveData<String>()
    val userList = MutableLiveData<List<GithubUser>>()
    val loading = MutableLiveData<Boolean>()

    init {
        // So that the user list is never null, only empty (no pesky null pointer exceptions)
        userList.value = emptyList()
    }
}