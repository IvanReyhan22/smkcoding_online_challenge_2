package com.ezyindustries.conews


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
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

class ExploreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiGetArticle()
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
        rv_listArticle.adapter = ArticleAdapter(context!!,article,"result")
        {
            val article = it
        }
    }
}