package com.example.githubusers.databinding

import agency.tango.android.avatarview.AvatarPlaceholder
import agency.tango.android.avatarview.views.AvatarView
import androidx.databinding.BindingAdapter
import com.example.githubusers.api.GithubUser
import com.squareup.picasso.Picasso

class AvatarViewBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("app:user")
        fun loadImage(view: AvatarView, user: GithubUser) {
            Picasso.get().load(user.avatarUrl).placeholder(AvatarPlaceholder(user.username)).fit().into(view)
        }

    }

}