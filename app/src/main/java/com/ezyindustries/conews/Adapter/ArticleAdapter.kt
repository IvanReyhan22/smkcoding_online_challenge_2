package com.ezyindustries.conews.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezyindustries.conews.Data.Article
import com.ezyindustries.conews.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.article_item_horizontal.*


class ArticleAdapter(private val context: Context, private val items: ArrayList<Article>, private val type: String) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(item: Article) {
            article_pic.setImageResource(R.drawable.ic_head)
            txtTitle.text = item.title
            txtDate.text = item.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(

        if (type.equals("horizontal")) {
            LayoutInflater.from(context).inflate(R.layout.article_item_horizontal, parent, false)
        } else if (type.equals("vertical")) {
            LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.article_item_search_result, parent, false)
        }

    )

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        holder.bindItem(items.get(position))
    }

}