package com.msa2002.firebasestoragepdf.room_db.dao

import androidx.room.*
import com.msa2002.firebasestoragepdf.models.PdfFile

@Dao
interface PdfDao {

    @Insert
    fun insertPdf(pdf: PdfFile)
//    @Query("Insert into Expenses Values(:id, :email, :place, :money, :date)")
//    fun insertExpense(id:Int, email:String, place:String, money:Double, date:String)

    @Query("Select * From PdfFile Order By pdf_uploadAt DESC")
    fun getAllPdfs() : MutableList<PdfFile>

    @Query("Delete From PdfFile")
    fun deleteAllFiles()

}