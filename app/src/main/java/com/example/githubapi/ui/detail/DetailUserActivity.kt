package com.example.githubapi.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.adapter.SectionPagerAdapter
import com.example.githubapi.databinding.ActivityDetailUserBinding
import com.example.githubapi.ui.SettingsManager
import com.example.githubapi.viewmodel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsManager = SettingsManager.getInstance(this)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar_url = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)
        username?.let { viewModel.setUserDetail(it) }
        viewModel.getUserDetail().observe(this) { detailUserResponse ->
            if (detailUserResponse != null) {
                binding.apply {
                    progressbarTop.visibility = View.GONE
                    tvName.text = detailUserResponse.name ?: ""
                    tvUsername.text = detailUserResponse.login ?: ""
                    tvFollowers.text = "${detailUserResponse.followers} Followers"
                    tvFollowing.text = "${detailUserResponse.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(detailUserResponse.avatar_url)
                        .centerCrop()
                        .into(ivProfile)
                }
            } else {
                binding.progressbarTop.visibility = View.VISIBLE
                showErrorMessage()
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            val safeUsername = username
            val safeAvatarUrl = avatar_url
            if (_isChecked) {
                viewModel.addToFavorite(safeUsername!!, id, safeAvatarUrl!!)
            } else {
                viewModel.removeFromFavorite(id)
            }

            binding.toggleFavorite.isChecked = _isChecked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }

        binding.tabs.setTabTextColors(settingsManager.getTabTextColors(this))
        binding.tabs.setSelectedTabIndicatorColor(settingsManager.getTabIndicatorColor(this))

        val toggleButton = findViewById<ToggleButton>(R.id.toggle_favorite)
        toggleButton.setButtonDrawable(settingsManager.getFavoriteButtonDrawableRes())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
    }
}
