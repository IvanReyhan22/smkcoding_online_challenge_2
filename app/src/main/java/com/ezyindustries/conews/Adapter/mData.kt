package com.ezyindustries.conews.Adapter

import android.content.Context
import android.content.SharedPreferences

class mData(var context: Context) {

    private var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences("Dummy", 0)
        editor = preferences.edit()
    }

    fun setString (key: String,value: String){
        editor.putString(key,value)
        editor.commit()
    }

    fun getString ( key : String):String {
        return preferences.getString(key, "")!!
    }

    fun removeString ( key: String) {
        editor.remove(key)!!
        editor.commit()
    }
}