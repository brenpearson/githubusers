package com.example.githubusers.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.api.GithubUser
import com.example.githubusers.recyclerview.UserAdapter

class RecyclerViewBinding {
    companion object {
        @BindingAdapter("data")
        @JvmStatic
        fun setData(recyclerView: RecyclerView, data: List<GithubUser>) {
            (recyclerView.adapter as? UserAdapter)?.setData(data)
        }
    }
}