package com.example.githubapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.datasources.model.User
import com.example.githubapi.service.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<User>>()
    private val searchResultUsers = MutableLiveData<ArrayList<User>>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
    }

    fun getSearchResultUsers(): LiveData<ArrayList<User>> {
        return searchResultUsers
    }

    fun setSearchUsers(query: String) {
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiInstance.getSearchUsers(query).execute()
                if (response.isSuccessful) {
                    searchResultUsers.postValue(response.body()?.items)
                }
            } catch (e: Exception) {
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun getUsers(page: Int) {
        if (!isLoading.value!!) {
            isLoading.value = true
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val perPage = 10
                    val offset = (page - 1) * perPage
                    val response = RetrofitClient.apiInstance.getUsers(perPage, offset).execute()
                    if (response.isSuccessful) {
                        val currentUsers = listUsers.value ?: ArrayList()
                        currentUsers.addAll(response.body() ?: emptyList())
                        listUsers.postValue(currentUsers)
                    }
                } catch (e: Exception) {
                } finally {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun getInitialUsersLiveData(): LiveData<ArrayList<User>> {
        return listUsers
    }
}
