package com.ezyindustries.conews.Data

import com.google.gson.annotations.SerializedName

data class UserData(

    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String

)