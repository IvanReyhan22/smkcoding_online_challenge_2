package com.ezyindustries.conews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezyindustries.conews.Adapter.ArticleAdapter
import com.ezyindustries.conews.Data.ArticleItem
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.ArticleService
import com.ezyindustries.conews.Util.InternetCheck
import com.ezyindustries.conews.Util.toast
import com.ezyindustries.conews.ViewModel.ArticleFragmentViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var ref: DatabaseReference
//    lateinit var articleData: ArrayList<ArticleModel>
    var articleData: MutableList<ArticleModel> = ArrayList()
    private val viewModel by viewModels<ArticleFragmentViewModel>()
    private var adapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.init(requireContext())

        //Internet connection access check
        checkConnection()

        //Xml config
        ViewProps()
    }

    private fun init() {
        rv_listArticleHorizontal.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(requireContext(), articleData, "horizontal") {
            val article = it

            val intent = Intent(context, ArticleDetail::class.java)

            val bundle = Bundle()

//            bundle.putString("ARTICLE_ID", article.articleId)
//            bundle.putString("OWNER_ID", article.ownerId)
            bundle.putString("TITLE", article.title)
            bundle.putString("CONTENT", article.content)
            bundle.putString("IMAGE", article.image)
            bundle.putString("DATE", article.date)

            intent.putExtras(bundle)

            startActivity(intent)
//            Log.e("Function init", it.toString())
        }
        rv_listArticleHorizontal.adapter = adapter
    }

    private fun checkConnection() {

        /**
         * Basically if there is an internet connection it will use apiGetArticle() method to get data from database
         * but if there's no internet connection then it will load data from room local database to show data list
         */
        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet!!) {
                    apiGetHotArticle()
                    apiGetDailyArtice()
                } else {
                    toast(context!!, "Tidak ada akses internet")

                    //Model CALL HOT ARTICLE FROM LOCAL DATABASE
                    viewModel.getHotArticle?.observe(viewLifecycleOwner, Observer { hotArticle ->
                        hotArticle.let {
                            adapter?.setData(it)
                        }
                        rv_listArticleHorizontal.layoutManager = LinearLayoutManager(context)
                        rv_listArticleHorizontal.adapter = adapter
                    })
                }
            }
        })

    }

    private fun ViewProps() {

        val headParam = head.layoutParams as ViewGroup.MarginLayoutParams
        headParam.setMargins(0, 60, 0, 110)
        head.layoutParams = headParam

        head.setImageResource(R.drawable.ic_head)

    }

    private fun apiGetHotArticle() {

        ref = FirebaseDatabase.getInstance().getReference("Article")

        ref.orderByChild("category").equalTo("hot").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast(context!!, "Connection Error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                articleData = java.util.ArrayList<ArticleModel>()
                for (snap in snapshot.children) {
                    val data = snap.getValue(ArticleModel::class.java)

                    articleData.add(data!!)
                }

                rv_listArticleHorizontal.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rv_listArticleHorizontal.adapter =
                    ArticleAdapter(context!!, articleData, "horizontal") {
                        val article = it
                        val intent = Intent(context, ArticleDetail::class.java)

                        val bundle = Bundle()
                        bundle.putString("TITLE", article.title)
                        bundle.putString("CONTENT", article.content)
                        bundle.putString("IMAGE", article.image)
                        bundle.putString("DATE", article.date)

                        intent.putExtras(bundle)

                        startActivity(intent)
                    }

            }

        })

//        ===========================================NODEJS API===============================================
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<ArticleService>(httpClient)
//        val call = apiRequest.getHotArticle()
//        call.enqueue(object : Callback<List<ArticleItem>> {
//
//            override fun onFailure(call: Call<List<ArticleItem>>, t: Throwable) {
//            }
//
//            override fun onResponse(
//                call: Call<List<ArticleItem>>, response:
//                Response<List<ArticleItem>>
//            ) {
//                when {
//                    response.isSuccessful ->
//                        when {
//                            response.body()?.size != 0 ->
//                                showHotArticle(response.body()!!)
//                            else -> {
//                                toast(context!!, "Berhasil")
//                            }
//                        }
//                    else -> {
//                        toast(context!!, "SERVER CLOSED")
//                    }
//                }
//            }
//        })
//        ===========================================NODEJS API===============================================
    }

    private fun apiGetDailyArtice() {

        ref = FirebaseDatabase.getInstance().getReference("Article")

        ref.orderByChild("category").equalTo("daily").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast(context!!, "Connection Error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                articleData = java.util.ArrayList<ArticleModel>()
                for (snap in snapshot.children) {
                    val data = snap.getValue(ArticleModel::class.java)

                    data?.key = snap.key!!
                    articleData.add(data!!)
                }
                viewModel.insertAll(articleData)

            }

        })

//        ===========================================NODEJS API===============================================
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<ArticleService>(httpClient)
//        val call = apiRequest.getDailyArticle()
//        call.enqueue(object : Callback<List<ArticleItem>> {
//
//            override fun onFailure(call: Call<List<ArticleItem>>, t: Throwable) {
//            }
//
//            override fun onResponse(
//                call: Call<List<ArticleItem>>, response:
//                Response<List<ArticleItem>>
//            ) {
//                when {
//                    response.isSuccessful ->
//                        when {
//                            response.body()?.size != 0 ->
//                                showDailyArticle(response.body()!!)
//                            else -> {
//                                toast(context!!, "Berhasil")
//                            }
//                        }
//                    else -> {
//                        toast(context!!, "SERVER CLOSED")
//                    }
//                }
//            }
//        })
//        ===========================================NODEJS API===============================================
    }


}