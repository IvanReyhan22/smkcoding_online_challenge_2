package com.ezyindustries.conews


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezyindustries.conews.Adapter.ArticleAdapter
import com.ezyindustries.conews.Data.ArticleItem
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.ArticleService
import com.ezyindustries.conews.Util.toast
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ExploreFragment : Fragment() {

    private var timer = Timer()
    private val DELAY: Long = 1000 // in m

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inpt_search_article.afterTextChanged {
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    searchArticle()
                }
            }, DELAY)
        }


        apiGetArticle()
    }

    private fun searchArticle() {
        val httpClient = httpClient()
        val apiRequest = apiRequest<ArticleService>(httpClient)

        val search = apiRequest.searchArticleBy(
            inpt_search_article.text.toString()
        )

        search.enqueue(object : Callback<List<ArticleItem>> {

            override fun onFailure(call: Call<List<ArticleItem>>, t: Throwable) {
                toast(context!!, "SERVER CLOSED " + t.message)
            }

            override fun onResponse(call: Call<List<ArticleItem>>, response: Response<List<ArticleItem>>) {

                when {
                    response.isSuccessful ->
                        when {
                            response.body()?.size != 0 ->
                                showArticle(response.body()!!)
                            else -> {
                                toast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        toast(context!!, "Failed get Data")
                    }
                }
            }


        })
    }

    private fun apiGetArticle() {

        val httpClient = httpClient()
        val apiRequest = apiRequest<ArticleService>(httpClient)
        val call = apiRequest.getArticle()
        call.enqueue(object : Callback<List<ArticleItem>> {
            override fun onFailure(call: Call<List<ArticleItem>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<ArticleItem>>, response:
                Response<List<ArticleItem>>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body()?.size != 0 ->
                                showArticle(response.body()!!)
                            else -> {
                                toast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        toast(context!!, "SERVER CLOSED")
                    }
                }
            }
        })
    }

    private fun showArticle(article: List<ArticleItem>) {
        rv_listArticle.layoutManager = LinearLayoutManager(context)
        rv_listArticle.adapter = ArticleAdapter(context!!, article, "result")
        {
            val article = it

            val intent = Intent(context, ArticleDetail::class.java)

            val bundle = Bundle()

            bundle.putString("ARTICLE_ID", article.articleId.toString())

            intent.putExtras(bundle)

            startActivity(intent)
        }
    }
}

private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}