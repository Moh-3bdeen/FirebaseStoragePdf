package com.msa2002.firebasestoragepdf.adapters

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.msa2002.firebasestoragepdf.databinding.RvPdfListBinding
import com.msa2002.firebasestoragepdf.models.PdfFile


class MyPdfAdapter(var activity: Activity, var data: ArrayList<PdfFile>) : RecyclerView.Adapter<MyPdfAdapter.ContactsViewHolder>() {
    inner class ContactsViewHolder(var binding: RvPdfListBinding) : RecyclerView.ViewHolder(binding.root)

    private val storageRef = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(RvPdfListBinding.inflate(LayoutInflater.from(activity), parent, false))
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val pdf = data[position]

        holder.binding.tvPdfName.text = pdf.name
        holder.binding.tvPdfDate.text = pdf.uploadAt

        holder.binding.cardOpenPdf.setOnClickListener {
            try {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdf.url))
                activity.startActivity(myIntent)
            } catch (e: Exception) {
                Toast.makeText(activity, "Can,t handle this request.\n${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        holder.binding.btnDownload.setOnClickListener {
            downloadFile(activity, pdf.name.toString(), pdf.url)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun downloadFile(context: Context, fileName: String, url: String?): Long {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(context, "pdf", "$fileName.pdf")
        return downloadManager.enqueue(request)
    }

}