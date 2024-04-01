package com.example.githubapi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubapi.datasources.local.FavoriteUser
import com.example.githubapi.datasources.local.FavoriteUserDao
import com.example.githubapi.datasources.local.UserDatabase
import com.example.githubapi.service.RetrofitClient
import com.example.githubapi.datasources.model.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val user = MutableLiveData<DetailUserResponse>()
    private var userDao: FavoriteUserDao?
    var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                }
            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(id, username, avatarUrl)
            userDao?.addToFavorite(user)
            Log.d("DetailUserViewModel", "User added to favorite: $user")
        }
    }

    suspend fun checkUser(id: Int): Int? {
        return userDao?.checkUser(id)
    }

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch() {
            userDao?.removeFromFavorite(id)
        }
    }
}