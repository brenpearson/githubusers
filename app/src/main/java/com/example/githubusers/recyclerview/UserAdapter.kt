package com.example.githubusers.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.R
import com.example.githubusers.api.GithubUser
import com.example.githubusers.databinding.MainActivityListener
import com.example.githubusers.databinding.UserItemBinding

class UserAdapter(val listener: MainActivityListener) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    var users = emptyList<GithubUser>()

    fun setData(items: List<GithubUser>) {
        users = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<UserItemBinding>(inflater, R.layout.user_item, parent, false)
        binding.listener = listener
        return UserHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(users[position])
    }

    class UserHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            binding.user = user
            binding.executePendingBindings()
        }
    }
}