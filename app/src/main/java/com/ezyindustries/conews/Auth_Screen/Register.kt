package com.ezyindustries.conews.Auth_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.MainActivity
import com.ezyindustries.conews.R
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        onClick()
    }

    private fun onClick() {

        back_btn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        login_btn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        register_btn.setOnClickListener {

            when {

                inpt_username.text.isEmpty() -> inpt_username.error = "Require"
                inpt_email.text.isEmpty() -> inpt_email.error = "Require"
                inpt_password.text.isEmpty() -> inpt_password.error = "Require"
                inpt_phone.text.isEmpty() -> inpt_phone.error = "Require"

                else -> registerHandler()
            }
        }
    }

    private fun registerHandler() {
        val intent = Intent(this, Login::class.java)

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val register = apiRequest.doRegister(
            inpt_email.text.toString(),
            inpt_password.text.toString(),
            inpt_username.text.toString(),
            inpt_phone.text.toString()
        )

        register.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(applicationContext,"FAILED" + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if (response.body()!!.userId > 0) {

                    startActivity(intent)
                    finish()

                } else {
                    toast(applicationContext,"Username atau password salah")
                }

            }

        })
    }
}
