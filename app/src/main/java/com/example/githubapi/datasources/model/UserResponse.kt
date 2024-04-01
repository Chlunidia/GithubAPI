package com.example.githubapi.datasources.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("items")
    val items: ArrayList<User>
)