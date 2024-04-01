package com.example.githubapi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.githubapi.viewmodel.FollowersViewModel
import com.example.githubapi.datasources.model.User
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FollowersViewModel

    @Before
    fun setup() {
        viewModel = FollowersViewModel()
    }

    @Test
    fun testGetListFollowersNotNull() {
        val followersLiveData: LiveData<ArrayList<User>> = viewModel.getListFollowers()
        assertNotNull(followersLiveData)
    }
}
