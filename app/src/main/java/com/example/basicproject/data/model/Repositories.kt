package com.example.basicproject.data.model

import com.google.gson.annotations.SerializedName

data class Repositories(
    val id: Long,
    val name: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val language: String
)
