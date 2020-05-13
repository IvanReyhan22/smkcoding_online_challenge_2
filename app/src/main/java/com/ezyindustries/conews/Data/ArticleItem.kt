package com.ezyindustries.conews.Data


import com.google.gson.annotations.SerializedName

data class ArticleItem(
    @SerializedName("article_id")
    val articleId: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String
)