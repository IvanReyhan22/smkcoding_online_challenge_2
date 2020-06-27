package com.ezyindustries.conews.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Database.ArticleDatabase
import com.ezyindustries.conews.Repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleFragmentViewModel() : ViewModel() {

    lateinit var repository: ArticleRepository

    lateinit var allArticle: LiveData<List<ArticleModel>>

    fun init(context: Context) {
        val articleDao = ArticleDatabase.getDatabase(context).myArticleDao()
        repository = ArticleRepository(articleDao)
        allArticle = repository.allArticle
    }

    fun delete(myFriend: ArticleModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(myFriend)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertAll(article: List<ArticleModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        repository.insertAll(article)
    }
}
