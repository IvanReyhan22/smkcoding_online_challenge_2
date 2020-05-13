package com.ezyindustries.conews.Auth_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezyindustries.conews.MainActivity
import com.ezyindustries.conews.R
import kotlinx.android.synthetic.main.activity_register.*

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
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
