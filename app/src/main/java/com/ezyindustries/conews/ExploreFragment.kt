package com.ezyindustries.conews


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezyindustries.conews.Adapter.ArticleAdapter
import com.ezyindustries.conews.Data.Article
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment() {

    lateinit var articleList : ArrayList<Article>

    private fun dummyData() {
        articleList = ArrayList()
        articleList.add(Article("Apa itu Corona? Kenapa Bahaya?","Covid-19","May 1","Description soon"))
        articleList.add(Article("Seputar PSBB","Covid-19","May 3","Description soon"))
        articleList.add(Article("Korban covid-19 masih bertambah","Covid-19","January 2 ","Description soon"))

    }

    private fun showArticle() {
        rv_listArticle.layoutManager = LinearLayoutManager(activity)
        rv_listArticle.adapter = ArticleAdapter(activity!!,articleList,"vertical")
    }

    private fun initView() {
        dummyData()
        showArticle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated( view: View, @Nullable savedInstanceState: Bundle? ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }
}