package com.msa2002.firebasestoragepdf

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.msa2002.firebasestoragepdf.adapters.MyPdfAdapter
import com.msa2002.firebasestoragepdf.databinding.ActivityMainBinding
import com.msa2002.firebasestoragepdf.fragments.BottomDialogFragment
import com.msa2002.firebasestoragepdf.room_db.database.DatabaseClient
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dialog: ProgressDialog
    private val storageRef = Firebase.storage.reference
    lateinit var selectedFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getALlFiles()

        dialog = ProgressDialog(this@MainActivity)
        dialog.setTitle("Uploading file")
        dialog.setMessage("Please wait...")
        dialog.setCancelable(false)

        binding.cardUploadFile.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "application/pdf"
            startActivityForResult(galleryIntent, 45)
        }

    }


    private fun getALlFiles() {
        val allFiles = DatabaseClient.getInstance(applicationContext)!!.appDatabase.pdfDao().getAllPdfs() as ArrayList
        if (allFiles.isEmpty()) {
            binding.filesRecyclerView.visibility = View.GONE
            binding.linearIfImpty.visibility = View.VISIBLE
        } else {
            binding.linearIfImpty.visibility = View.GONE
            binding.filesRecyclerView.visibility = View.VISIBLE

            var myContactsAdapter = MyPdfAdapter(this, allFiles)
            binding.filesRecyclerView.adapter = myContactsAdapter
            binding.filesRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 45) {
            dialog.show()
            if (data != null) {
                if (data.data != null) {
                    val dateTime = SimpleDateFormat("yyyy/MM/dd - HH:mm:SS").format(Date())

                    val filePath = data.data // File Path
                    Log.e("msaPath", "Path file is: $filePath")
                    val calendar = Calendar.getInstance()
                    val reference = storageRef.child("Files")
                        .child(calendar.timeInMillis.toString())
                    reference.putFile(filePath!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                reference.downloadUrl.addOnCompleteListener { uri ->
                                    selectedFile = uri.result.toString()
                                    Toast.makeText(applicationContext, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()

                                    val bottomDialog = BottomDialogFragment(object :
                                        BottomDialogFragment.OnClickListener {
                                        // عشان يحدث على الواجهة بعد كل عملية اضافة
                                        override fun isAddFile(isUpload: Boolean) {
                                            if (isUpload) {
                                                getALlFiles()
                                            }
                                        }
                                    })
                                    val args = Bundle()
                                    args.putString("selectedFile", selectedFile)
                                    args.putString("dateTime", dateTime)
                                    bottomDialog.arguments = args
                                    bottomDialog.show(supportFragmentManager, "uploadFile")
                                }
                            }
                        }

                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Uploaded Failed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss()
                        }
                }
            }
        }
    }
}