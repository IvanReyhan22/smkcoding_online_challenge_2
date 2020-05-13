package com.ezyindustries.conews.Service

import com.ezyindustries.conews.Data.ArticleItem
import retrofit2.Call
import retrofit2.http.GET

interface ArticleService{

    @GET("article")
    fun getArticle(): Call<List<ArticleItem>>
}