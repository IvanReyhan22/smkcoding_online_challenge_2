package com.ezyindustries.conews.Auth_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezyindustries.conews.MainActivity
import com.ezyindustries.conews.R
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onClick()
    }

    private fun onClick() {
        register_btn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        login_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
