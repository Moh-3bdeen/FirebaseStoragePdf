package com.msa2002.firebasestoragepdf.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PdfFile{
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    @ColumnInfo(name = "pdf_name") var name: String? = null
    @ColumnInfo(name = "pdf_url") var url: String? = null
    @ColumnInfo(name = "pdf_uploadAt") var uploadAt: String? = null
}