package com.ezyindustries.conews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.Data.UserModel
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEdit : AppCompatActivity() {

    lateinit var ref: DatabaseReference

    //    private var user_id = ""
    private var bUserId = ""
    private var bName = ""
    private var bImage = ""
    private var bEmail = ""
    private var bCaption = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

//        val sharedPreferences = mData(applicationContext)
//        user_id = sharedPreferences.getString("USER_ID")

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

        cancel_btn.setOnClickListener{
            getProfileData()
        }
    }

    private fun updateUser() {
        ref = FirebaseDatabase.getInstance().getReference("User")

        val data = UserModel(
            bUserId,
            bImage,
            inpt_name.text.toString(),
            inpt_email.text.toString(),
            inpt_caption.text.toString()
        )

        ref.child(bUserId).setValue(data).addOnCompleteListener {
            toast(this, "Update Profile sukses")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("viewpager_position", 4)
            startActivity(intent)
        }
//        ===========================================NODEJS API===============================================
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<AuthService>(httpClient)
//
//        val update = apiRequest.updateUser(
//            user_id,
//            inpt_email.text.toString(),
//            inpt_name.text.toString(),
//            inpt_phone.text.toString(),
//            inpt_caption.text.toString()
//        )
//
//        update.enqueue(object : Callback<UserData> {
//            override fun onFailure(call: Call<UserData>, t: Throwable) {
//                toast(applicationContext, "SERVER CLOSED " + t.message)
//            }
//
//            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//
//                if (response.body()!!.status.equals("true")) {
//
//                    toast(applicationContext, "Update success")
//
//                } else {
//
//                    toast(applicationContext, "Failed update, Try again")
//
//                }
//
//            }
//
//        })
//        ===========================================NODEJS API===============================================

    }

    private fun getProfileData() {

        val bundle = intent.extras
        bUserId = bundle?.getString("USERID").toString()
        bName = bundle?.getString("NAME").toString()
        bImage = bundle?.getString("IMAGE").toString()
        bEmail = bundle?.getString("EMAIL").toString()
        bCaption = bundle?.getString("CAPTION").toString()

        inpt_name.setText(bName)
        inpt_email.setText(bEmail)
        if (bCaption != "") {
            inpt_caption.setText(bCaption)
        }
        Glide.with(this)
            .load(bImage)
            .into(profile_pic)

//        ===========================================NODEJS API===============================================
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<AuthService>(httpClient)
//
//        val getProfile = apiRequest.getUserById(
//            user_id
//        )
//
//        getProfile.enqueue(object : Callback<UserData> {
//            override fun onFailure(call: Call<UserData>, t: Throwable) {
//                toast(applicationContext, "FAILED SERVER CLOSED" + t.message)
//            }
//
//            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//
//                if (response.body()!!.userId > 0) {
//
//                    val item = response.body()!!
//
//                    inpt_name.setText(item.username)
//                    inpt_email.setText(item.email)
//                    inpt_phone.setText(item.phone)
//
//                    if (item.caption != "null") {
//                        inpt_caption.setText(applicationContext.getString(R.string.caption_default))
//                    }
//
//
//                } else {
//                    toast(applicationContext, "PLEASE RESTART")
//                }
//            }
//
//        })
//        ===========================================NODEJS API===============================================

    }

}
