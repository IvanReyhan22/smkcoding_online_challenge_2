package com.ezyindustries.conews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ezyindustries.conews.Data.ArticleItem
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.ArticleService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.activity_article_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleDetail : AppCompatActivity() {

    private var articleId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val bundle = intent.extras
        articleId = bundle.getString("ARTICLE_ID")

        getArticleData()
        onClick()

    }

    private fun onClick() {
        back_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getArticleData() {

        val httpClient = httpClient()
        val apiRequest = apiRequest<ArticleService>(httpClient)

        val getArticle = apiRequest.getArticleById(
            articleId
        )

        getArticle.enqueue(object : Callback<ArticleItem>{
            override fun onFailure(call: Call<ArticleItem>, t: Throwable) {
                toast(applicationContext,"FAILED SERVER CLOSED" + t.message)
            }

            override fun onResponse(call: Call<ArticleItem>, response: Response<ArticleItem>) {

                if (response.body()!!.articleId > 0) {

                    val item = response.body()!!

                    date.text =  item.date
                    article_title.text = item.title
                    article_description.text = item.description

                    Glide.with(applicationContext)
                        .load(applicationContext.getString(R.string.base_storage_url) + item.image)
                        .into(article_pic)

                } else {

                    toast(applicationContext,"No data please Restart!")

                }

            }

        })

    }
}
