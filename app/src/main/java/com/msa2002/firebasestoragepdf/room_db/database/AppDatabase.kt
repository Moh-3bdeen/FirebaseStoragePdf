package com.msa2002.firebasestoragepdf.room_db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msa2002.firebasestoragepdf.models.PdfFile
import com.msa2002.firebasestoragepdf.room_db.dao.PdfDao

@Database(entities = [PdfFile::class], version = 1)
abstract class AppDatabase :RoomDatabase() {
    abstract fun pdfDao() : PdfDao
}