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

    private var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val sharedPreferences = mData(applicationContext)

        user_id = sharedPreferences.getString("USER_ID")

        getProfileData()
        onClick()
    }

    private fun onClick() {
        back_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        submit_btn.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val update = apiRequest.updateUser(
            user_id,
            inpt_email.text.toString(),
            inpt_name.text.toString(),
            inpt_phone.text.toString(),
            inpt_caption.text.toString()
        )

        update.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(applicationContext, "SERVER CLOSED " + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if (response.body()!!.status.equals("true")) {

                    toast(applicationContext, "Update success")

                } else {

                    toast(applicationContext, "Failed update, Try again")

                }

            }

        })

    }

    private fun getProfileData() {

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val getProfile = apiRequest.getUserById(
            user_id
        )

        getProfile.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(applicationContext, "FAILED SERVER CLOSED" + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if (response.body()!!.userId > 0) {

                    val item = response.body()!!

                    inpt_name.setText(item.username)
                    inpt_email.setText(item.email)
                    inpt_phone.setText(item.phone)

                    if (item.caption != "null") {
                        inpt_caption.setText(applicationContext.getString(R.string.caption_default))
                    }


                } else {
                    toast(applicationContext, "PLEASE RESTART")
                }
            }

        })

    }

}
