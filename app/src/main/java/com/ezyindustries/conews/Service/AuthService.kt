package com.ezyindustries.conews.Service

import com.ezyindustries.conews.Data.UserData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthService{

    @POST("login")
    fun doLogin(
        @Field("eml")email:String,
        @Field("pass")password:String
    ):Call<UserData>

    @POST("register")
    fun doRegister(
        @Field("email")email:String,
        @Field("password")password:String,
        @Field("username")username:String,
        @Field("phone")phone:String
    ):Call<UserData>
}