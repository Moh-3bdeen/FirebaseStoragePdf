package com.msa2002.firebasestoragepdf.room_db.database

import android.content.Context
import androidx.room.Room

class DatabaseClient(context: Context) {
    val appDatabase: AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "MyPdfs_db")
            .allowMainThreadQueries().build()

    companion object {
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(context: Context?): DatabaseClient? {
            if (mInstance == null) {
                mInstance = DatabaseClient(context!!)
            }
            return mInstance!!
        }
    }
}