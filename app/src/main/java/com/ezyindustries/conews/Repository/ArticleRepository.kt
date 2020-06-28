package com.ezyindustries.conews.Repository

import androidx.lifecycle.LiveData
import com.ezyindustries.conews.Dao.ArticleDao
import com.ezyindustries.conews.Data.ArticleModel

class ArticleRepository(private val articleDao: ArticleDao) {
    val allArticle: LiveData<List<ArticleModel>> = articleDao.getArticle()

    val hotArticle: LiveData<List<ArticleModel>> = articleDao.getHotArticle("hot")

    suspend fun insert(article: ArticleModel){
        articleDao.insert(article)
    }

    suspend fun insertAll(article: List<ArticleModel>) {
        articleDao.insertAll(article)
    }

    suspend fun deleteAll() {
        articleDao.deleteAll()
    }

    suspend fun update(article: ArticleModel) {
        articleDao.update(article)
    }

    suspend fun delete(article: ArticleModel) {
        articleDao.delete(article)
    }


}