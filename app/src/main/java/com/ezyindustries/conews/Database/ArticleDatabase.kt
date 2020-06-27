package com.ezyindustries.conews.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ezyindustries.conews.Dao.ArticleDao
import com.ezyindustries.conews.Data.ArticleModel

@Database(entities = arrayOf(ArticleModel::class), version = 1, exportSchema = false)
public abstract class ArticleDatabase : RoomDatabase() {

    abstract fun myArticleDao(): ArticleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
