package com.ezyindustries.conews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_article_detail.*

class ArticleDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        onClick()
    }

    private fun onClick() {
        back_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
