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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezyindustries.conews.Adapter.ArticleAdapter
import com.ezyindustries.conews.Data.ArticleItem
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.ArticleService
import com.ezyindustries.conews.Util.toast
import com.ezyindustries.conews.ViewModel.ArticleFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ExploreFragment : Fragment(){

    private var timer = Timer()
    private val DELAY: Long = 1000 // in m

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

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(requireContext());
        viewModel.allArticle.observe(viewLifecycleOwner, androidx.lifecycle.Observer { article ->
            article?.let(adapter?.setData(it))
        })

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

    private fun init() {
        rv_listArticle.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(requireContext(),articleData)
        rv_listArticle.adapter = adapter
//        adapter?.listener = this
    }

    private fun searchArticle() {

//        ===========================================NODEJS API===============================================
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<ArticleService>(httpClient)
//
//        val search = apiRequest.searchArticleBy(
//            inpt_search_article.text.toString()
//        )
//
//        search.enqueue(object : Callback<List<ArticleItem>> {
//
//            override fun onFailure(call: Call<List<ArticleItem>>, t: Throwable) {
//                toast(context!!, "SERVER CLOSED " + t.message)
//            }
//
//            override fun onResponse(call: Call<List<ArticleItem>>, response: Response<List<ArticleItem>>) {
//
//                when {
//                    response.isSuccessful ->
//                        when {
//                            response.body()?.size != 0 ->
//                                showArticle(response.body()!!)
//                            else -> {
//                                toast(context!!, "Berhasil")
//                            }
//                        }
//                    else -> {
//                        toast(context!!, "Failed get Data")
//                    }
//                }
//            }
//
//
//        })
//        ===========================================NODEJS API===============================================
    }

    private fun apiGetArticle() {

        ref = FirebaseDatabase.getInstance().getReference("Article")

        ref.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast(context!!, "Connection Error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                articleData = java.util.ArrayList<ArticleModel>()
                for (snap in snapshot.children) {
                    val data = snap.getValue(ArticleModel::class.java)

                    data?.articleId = snap.key!!
                    articleData.add(data!!)
                }
                viewModel.insertAll(articleData)

                rv_listArticle.layoutManager = LinearLayoutManager(context)
                rv_listArticle.adapter = ArticleAdapter(context!!, articleData, "result") {
                    val article = it

                    val intent = Intent(context, ArticleDetail::class.java)

                    val bundle = Bundle()

//                    bundle.putString("ARTICLE_ID", article.articleId)
//                    bundle.putString("OWNER_ID", article.ownerId)
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
//        val call = apiRequest.getArticle()
//        call.enqueue(object : Callback<List<ArticleItem>> {
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
//                                showArticle(response.body()!!)
//                        }
//                    else -> {
//                        toast(context!!, "SERVER CLOSED")
//                    }
//                }
//            }
//        })
//        ===========================================NODEJS API===============================================
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


}