package com.ezyindustries.conews.Data

data class UserModel(
    var userId: String,
    var image:String,
    var name: String,
    var email: String,
    var caption: String
){
    constructor() : this("","","","","")
}


