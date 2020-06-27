package com.ezyindustries.conews.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleModel(
    var articleId: String,
    var ownerId: String,
    var title: String,
    var content: String,
    var image: String,
    var category: String,
    var date: String,
    @PrimaryKey var key:String
){
    constructor() : this("","","","","","","","")
}
