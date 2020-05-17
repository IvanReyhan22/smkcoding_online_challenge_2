package com.ezyindustries.conews.Auth_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.MainActivity
import com.ezyindustries.conews.R
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private val emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isLoggedIn()
        onClick()

    }

    private fun isLoggedIn(){
        val sharedPreferences = mData(applicationContext)

        val user_id = sharedPreferences.getString("USER_ID")

        if (user_id != "") {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun onClick() {
        register_btn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        login_btn.setOnClickListener {

            when {
                inpt_email.text.isEmpty() -> inpt_email.error = "Require"
                inpt_password.text.isEmpty() -> inpt_password.error = "Require"
                !inpt_email.text.toString().trim().matches(emailPattern.toRegex()) ->  inpt_email.error = "Not Valid Email"

                else -> loginHandler()
            }

        }
    }

    private fun loginHandler() {

        val intent = Intent(this, MainActivity::class.java)

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val login = apiRequest.doLogin(
            inpt_email.text.toString(),
            inpt_password.text.toString()
        )

        login.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(applicationContext,"FAILED SERVER CLOSED" + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if (response.body()!!.userId > 0) {

                    val data = mData(applicationContext)

                    data.setString("USER_ID",response.body()!!.userId.toString())
                    data.setString("USERNAME",response.body()!!.username)
                    data.setString("USER_EMAIL",response.body()!!.email)
                    data.setString("USER_PHONE",response.body()!!.password)

                    startActivity(intent)
                    finish()

                } else if(response.body()!!.status.equals("false")){

                    toast(applicationContext,response.body()!!.values)

                } else {

                    toast(applicationContext,"No Internet or Server Down")

                }

            }

        })

    }

}
