package com.ezyindustries.conews.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezyindustries.conews.Data.Article
import com.ezyindustries.conews.Data.ArticleModel
import kotlinx.android.extensions.LayoutContainer
import com.ezyindustries.conews.R
import com.ezyindustries.conews.Util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.article_item_horizontal.*

class ArticleAdapter(
    private val context: Context, private var items:

    List<ArticleModel>, private val type: String, private val listener: (ArticleModel) -> Unit
) :

    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    fun setData(list: List<ArticleModel>){
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (type.equals("horizontal")) {
            return ViewHolder(
                context,
                LayoutInflater.from(context)
                    .inflate(R.layout.article_item_horizontal, parent, false)
            )
        } else if (type.equals("vertical")) {
            return ViewHolder(
                context,
                LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
            )
        } else if (type.equals("owner")) {
            return ViewHolder(
                context,
                LayoutInflater.from(context).inflate(R.layout.article_item_owner, parent, false)
            )
        } else {
            return return ViewHolder(
                context,
                LayoutInflater.from(context)
                    .inflate(R.layout.article_item_search_result, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)

        val ArticleId: String = items.get(position).articleId
        val OwnerId: String = items.get(position).ownerId
        var Title: String = items.get(position).title
        val Content: String = items.get(position).content
        val Image: String = items.get(position).image
        val Category: String = items.get(position).category
        val Date: String = items.get(position).date

        if(type.equals("vertical")) {
            if (Title.length > 40) {
                Title = Title.substring(0,40) + "..."
            }
        }

        holder.txtTitle.setText(Title)
        holder.txtDate.setText(Date)
        Glide.with(context)
            .load(Image)
            .into(holder.article_pic)

//        val listener: dataListener? = null
//        lateinit var ref : DatabaseReference
//        lateinit var auth: FirebaseAuth

    }

    fun <T : RecyclerView.ViewHolder> T.listen(
        event: (position: Int, type: Int) -> Unit
    ): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(item: ArticleModel, listener: (ArticleModel) -> Unit) {
            txtTitle.text = item.title
            txtDate.text = item.date

            Glide.with(context)
                .load(item.image)
                .into(article_pic)

            containerView.setOnClickListener { listener(item) }
        }
//=======================================NODEJS API================================================
//        fun bindItem(item: ArticleItem, listener: (ArticleItem) -> Unit) {
//            txtTitle.text = item.title
//            txtDate.text = item.date
//
//            Glide.with(context)
//                .load(context.getString(R.string.base_storage_url) + item.image)
//                .into(article_pic)
//
//            containerView.setOnClickListener { listener(item) }
//        }
//=======================================NODEJS API================================================

    }

}

//        if (type.equals("horizontal")) {
//            LayoutInflater.from(context).inflate(R.layout.article_item_horizontal, parent, false)
//        } else if (type.equals("vertical")) {
//            LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
//        } else {
//            LayoutInflater.from(context).inflate(R.layout.article_item_search_result, parent, false)
//        }