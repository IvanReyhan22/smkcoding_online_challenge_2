package com.ezyindustries.conews


import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Auth_Screen.Login
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileData()
        onClick()
    }

    private fun onClick() {

        logout.setOnClickListener {

            val data = mData(context!!)

            data.removeString("USER_ID")
            data.removeString("USERNAME")
            data.removeString("USER_EMAIL")
            data.removeString("USER_PHONE")

            startActivity(Intent(context,Login::class.java))
            activity!!.finish()
        }
    }

    private fun getProfileData() {

        val sharedPreferences = mData(context!!)

        val user_id = sharedPreferences.getString("USER_ID")

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val getProfile = apiRequest.getUserById(
            user_id
        )

        getProfile.enqueue(object: Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(context!!,"FAILED SERVER CLOSED" + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if ( response.body()!!.userId > 0) {

                    val item = response.body()!!

                    name.text = item.username
                    email.text = item.email

                    if (item.caption != null) {
                        caption.text = item.caption
                    }


                } else {
                    toast(context!!,"PLEASE RESTART")
                }
            }

        })
    }
}