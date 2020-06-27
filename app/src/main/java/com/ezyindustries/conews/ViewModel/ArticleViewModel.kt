package com.ezyindustries.conews.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Database.ArticleDatabase
import com.ezyindustries.conews.Repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {
    lateinit var repository: ArticleRepository

    fun init(context: Context) {
        val articleDao = ArticleDatabase.getDatabase(context).myArticleDao()
        repository = ArticleRepository(articleDao)
    }

    fun addData(article: ArticleModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(article)
        }
    }
}