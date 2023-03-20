package com.msa2002.firebasestoragepdf.fragments

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.msa2002.firebasestoragepdf.databinding.FragmentBottomDialogBinding
import com.msa2002.firebasestoragepdf.models.PdfFile
import com.msa2002.firebasestoragepdf.room_db.database.DatabaseClient
import java.util.*

class BottomDialogFragment(var onClickListener: OnClickListener) : BottomSheetDialogFragment() {
    interface OnClickListener {
        fun isAddFile(isUpload: Boolean)
    }

    private lateinit var binding: FragmentBottomDialogBinding
    var isCLickSave: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomDialogBinding.inflate(inflater, container, false)

        val mArgs = arguments
        val selectedFile = mArgs!!.getString("selectedFile")
        val dateTime = mArgs!!.getString("dateTime")

        binding.etFileName.setText("$dateTime")


        binding.btnSaveName.setOnClickListener {
            val fileName = binding.etFileName.text.toString().trim()
            if (fileName.isEmpty()) {
                binding.etFileName.error = "Write name !"
            } else {
                isCLickSave = true

                val pdf = PdfFile()
                pdf.name = fileName
                pdf.url = selectedFile
                pdf.uploadAt = dateTime
                DatabaseClient.getInstance(requireContext())!!.appDatabase.pdfDao().insertPdf(pdf)
                Toast.makeText(requireContext(), "Rename file Successfully", Toast.LENGTH_SHORT).show()

                onClickListener.isAddFile(true)
                dismiss()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isCLickSave) {
            val mArgs = arguments
            val selectedFile = mArgs!!.getString("selectedFile")
            val dateTime = mArgs!!.getString("dateTime")

            val pdf = PdfFile()
            pdf.name = dateTime
            pdf.url = selectedFile
            pdf.uploadAt = dateTime

            DatabaseClient.getInstance(requireContext())!!.appDatabase.pdfDao().insertPdf(pdf)
            Toast.makeText(requireContext(), "Rename file Successfully", Toast.LENGTH_SHORT).show()

            onClickListener.isAddFile(true)
            dismiss()
        }

    }
}