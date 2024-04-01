package com.example.githubapi.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.R
import com.example.githubapi.adapter.UserAdapter
import com.example.githubapi.ui.SettingsManager
import com.example.githubapi.datasources.model.User
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.ui.detail.DetailUserActivity
import com.example.githubapi.ui.favorite.FavoriteActivity
import com.example.githubapi.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var settingsManager: SettingsManager
    private var isLoading = false
    private var isFirstLoad = true
    private val PER_PAGE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsManager = SettingsManager.getInstance(this)
        settingsManager.setToolbarLogo(binding.toolbarLogo)
        setupRecyclerView()
        setupViewModel()
        loadInitialUsers()
        setupSearchView()
        setSupportActionBar(binding.toolbar)
        applyTheme()
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingProgress()
            } else {
                hideLoadingProgress()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val themeSwitch = (menu?.findItem(R.id.theme_switch)?.actionView as? SwitchCompat)
            ?: findViewById(R.id.theme_switch)
        settingsManager.setThemeSwitchListener(themeSwitch)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        settingsManager.updateFavoriteIcon(menu, isDarkTheme())
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyTheme()
    }

    private fun isDarkTheme(): Boolean {
        return settingsManager.getThemeSetting()
    }

    private fun applyTheme() {
        settingsManager.applyTheme(isDarkTheme())
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!isLoading && totalItemCount <= lastVisibleItemPosition + 1) {
                    loadNextUsers()
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getInitialUsersLiveData().observe(this) { users ->
            if (isFirstLoad) {
                adapter.setUserList(users)
                isFirstLoad = false
                showLoading(false)
            }
        }
        viewModel.getSearchResultUsers().observe(this) { searchResults ->
            if (binding.searchView.query.isNullOrEmpty()) {
                showLoading(true)
                viewModel.getUsers(1)
            } else {
                adapter.setUserList(searchResults)
                showLoading(false)
            }
        }
    }

    private fun loadInitialUsers() {
        if (binding.searchView.query.isBlank()) {
            showLoading(true)
            viewModel.getUsers(1)
        }
    }

    private fun loadNextUsers() {
        if (!isLoading) {
            isLoading = true
            val nextPage = (adapter.itemCount / PER_PAGE) + 1
            viewModel.getUsers(nextPage)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    showLoading(true)
                    viewModel.setSearchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.getUsers(1)
                }
                return true
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLoadingProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        binding.progressBar.visibility = View.GONE
    }
}
