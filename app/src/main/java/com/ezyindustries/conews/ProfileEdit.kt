package com.ezyindustries.conews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.activity_profile_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEdit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        onClick()
    }

    private fun onClick() {
        back_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
