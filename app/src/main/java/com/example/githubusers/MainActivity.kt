package com.example.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.api.GithubResponse
import com.example.githubusers.api.GithubService
import com.example.githubusers.databinding.ActivityMainBinding
import com.example.githubusers.databinding.MainActivityViewModel
import com.example.githubusers.recyclerview.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.RecyclerView
import agency.tango.android.avatarview.loader.PicassoLoader
import agency.tango.android.avatarviewbindings.bindings.AvatarViewBindings
import android.net.Uri
import android.transition.Transition
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.transition.TransitionManager
import com.example.githubusers.api.GithubUser
import com.example.githubusers.databinding.MainActivityListener
import com.saurabharora.customtabs.ConnectionCallback
import com.saurabharora.customtabs.CustomTabActivityHelper
import com.saurabharora.customtabs.extensions.launchWithFallback


class MainActivity : AppCompatActivity(), Callback<GithubResponse>, MainActivityListener {

    val TAG = "MainActivity"

    lateinit var binding:  ActivityMainBinding

    val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).build()
    val github = retrofit.create(GithubService::class.java)
    val handler = Handler()
    var searchJob: Runnable? = null

    val initialSet = ConstraintSet()
    val searchingSet = ConstraintSet()
    var changedSet = false

    private val  customTabActivityHelper = CustomTabActivityHelper(this, lifecycle, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = MainActivityViewModel()
        binding.lifecycleOwner = this

        setupAppBar()
        setupConstraintSets()

        // Set up recycler view
        recycler_view.adapter = UserAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Observe changes to the search query
        binding.viewModel?.searchText?.observe(this, Observer { updateSearch(it) })

    }

    private fun setupAppBar() {
        setSupportActionBar(toolbar)

        // Only lift the app bar on scroll - makes the page look nicer ^_^
        app_bar.setLiftable(true)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(view: RecyclerView, scrollState: Int) {}
            override fun onScrolled(view: RecyclerView, firstVisibleItem: Int, dy: Int) {
                val isAtTop = firstVisibleItem == 0 && (view.childCount == 0 || view.getChildAt(0).top == 0)
                app_bar.setLifted(!isAtTop)
            }
        })
    }

    private fun setupConstraintSets() {
        // Keep the initial layout for later to revert back
        initialSet.clone(constraint_layout)
        initialSet.setVisibility(R.id.content_frame, View.INVISIBLE)

        searchingSet.clone(constraint_layout)
        searchingSet.setVisibility(R.id.content_frame, View.VISIBLE)
        // Clear the app bar's bottom constraint so it animates back to the top of the screen
        searchingSet.clear(R.id.app_bar, ConstraintSet.BOTTOM)
    }

    private fun updateSearch(query: String) {
        // Cancel the pending search job as the query has changed
        searchJob?.let { handler.removeCallbacks(searchJob) }

        if (query.length >= 3) {
            // Check if we need to animate the constraint layout
            if (!changedSet) {
                changedSet = true
                TransitionManager.beginDelayedTransition(constraint_layout)
                searchingSet.applyTo(constraint_layout)
            }

            binding.viewModel?.loading?.value = true
            app_bar.setLifted(false)
            binding.executePendingBindings()
            searchJob = Runnable {
                // Search for users, just first page and 100 results for now - we can always add infinite scroll later ;)
                github.searchUsers(query, 0, 100).enqueue(this)
                searchJob = null
            }

            // Hold up.. wait a second.. before searching, the user might still be typing
            handler.postDelayed (searchJob, 1000)
        } else {
            // Check if we need to animate the constraint layout back
            if (changedSet) {
                changedSet = false
                TransitionManager.beginDelayedTransition(constraint_layout)
                initialSet.applyTo(constraint_layout)
            }

            binding.viewModel?.loading?.value = false
            binding.viewModel?.userList?.value = emptyList()
            binding.executePendingBindings()
        }
    }

    override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
        Log.e(TAG, t.toString())

        binding.viewModel?.userList?.value = emptyList()
        binding.viewModel?.loading?.value = false
        binding.executePendingBindings()
    }

    override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
        if (response.isSuccessful) {
            Log.i(TAG, "Response successful")

            binding.viewModel?.userList?.value = response.body()?.users?: emptyList()
            binding.viewModel?.loading?.value = false
            binding.executePendingBindings()
        }
    }

    override fun onUserClick(user: GithubUser) {
        // Open the user's github page in a chrome tab
        val customTabsIntent = CustomTabsIntent.Builder(customTabActivityHelper.session).build()
        customTabsIntent.launchWithFallback(this, Uri.parse(user.profileUrl))
    }
}
