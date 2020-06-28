package com.ezyindustries.conews.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey var userId: String,
    var image:String,
    var name: String,
    var email: String,
    var caption: String
){
    constructor() : this("","","","","")
}


