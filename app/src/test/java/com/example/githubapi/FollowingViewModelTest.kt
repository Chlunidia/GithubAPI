package com.example.githubapi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.githubapi.viewmodel.FollowingViewModel
import com.example.githubapi.datasources.model.User
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FollowingViewModel

    @Before
    fun setup() {
        viewModel = FollowingViewModel()
    }

    @Test
    fun testGetListFollowingNotNull() {
        val followingLiveData: LiveData<ArrayList<User>> = viewModel.getListFollowing()

        assertNotNull(followingLiveData)
    }
}
