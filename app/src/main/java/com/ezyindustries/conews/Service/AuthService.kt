package com.ezyindustries.conews.Service

import com.ezyindustries.conews.Data.UserData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService{

    @FormUrlEncoded
    @POST("login")
    fun doLogin(
        @Field("eml")email:String,
        @Field("pass")password:String
    ):Call<UserData>

    @FormUrlEncoded
    @POST("register")
    fun doRegister(
        @Field("email")email:String,
        @Field("password")password:String,
        @Field("username")username:String,
        @Field("phone")phone:String
    ):Call<UserData>

    @FormUrlEncoded
    @POST("userById")
    fun getUserById(
        @Field("user_id")user_id:String
    ):Call<UserData>
}