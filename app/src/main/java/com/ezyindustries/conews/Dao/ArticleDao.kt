package com.ezyindustries.conews.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ezyindustries.conews.Data.ArticleModel

@Dao
interface ArticleDao {

    @Query("SELECT * from article")
    fun getArticle(): LiveData<List<ArticleModel>>

    @Query("SELECT * from article WHERE category = :category")
    fun getHotArticle(category: String): LiveData<List<ArticleModel>>

    @Query("SELECT * from article WHERE ownerId = :ownerId")
    fun getUserArticle(ownerId: String): LiveData<List<ArticleModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(article: List<ArticleModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(article: ArticleModel)

    @Delete()
    suspend fun delete(article: ArticleModel)

    @Query("DELETE FROM article")
    suspend fun deleteAll()


}