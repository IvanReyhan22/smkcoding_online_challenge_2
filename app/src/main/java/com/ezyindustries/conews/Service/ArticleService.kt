package com.ezyindustries.conews.Service

import com.ezyindustries.conews.Data.ArticleItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ArticleService {

    @GET("article")
    fun getArticle(): Call<List<ArticleItem>>

    @FormUrlEncoded
    @POST("articleById")
    fun getArticleById(
        @Field("article_id") user_id: String
    ): Call<ArticleItem>

    @FormUrlEncoded
    @POST("article/search")
    fun searchArticleBy(
        @Field("search") search: String
    ): Call<List<ArticleItem>>
}