package com.ezyindustries.conews

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Util.toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_article.*
import java.io.IOException
import java.util.*

class EditArticle : AppCompatActivity() {

    //IMAGE REFERENCE
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var ref: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var bunArticleId = ""
    private var bunOwnerId = ""
    private var bunTitle = ""
    private var bunContent = ""
    private var bunImage = ""
    private var newImage = ""
    private var bunDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_article)

        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        getData()

        setOnClickListener()
    }

    private fun getData() {

        val bundle = intent.extras
        bunArticleId = bundle?.getString("ARTICLE_ID").toString()
        bunOwnerId = bundle?.getString("OWNER_ID").toString()
        bunTitle = bundle?.getString("TITLE").toString()
        bunContent = bundle?.getString("CONTENT").toString()
        bunImage = bundle?.getString("IMAGE").toString()
        bunDate = bundle?.getString("DATE").toString()

        inptTitle.setText(bunTitle)
        inptContent.setText(bunContent)

        Glide.with(applicationContext)
            .load(bunImage)
            .into(image_preview)
    }

    private fun deleteArticle() {
        ref = FirebaseDatabase.getInstance().getReference("Article")

        ref.child(bunArticleId).removeValue().addOnCompleteListener {
            toast(this, "Artikel berhasil dihapus")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateArticle() {
        ref = FirebaseDatabase.getInstance().getReference("Article")

        val user = firebaseAuth.currentUser

        //INPUT
        val refTitle = inptTitle.text.toString()
        val refContent = inptContent.text.toString()

        val data = ArticleModel(
            bunArticleId,
            user!!.uid,
            refTitle,
            refContent,
            newImage,
            "daily",
            bunDate
        )

        ref.child(bunArticleId).setValue(data).addOnCompleteListener {
            toast(this, "Update artikel sukses")

            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("viewpager_position", 3)
            startActivity(intent)
        }
    }

    private fun imagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("articleImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

//            val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    newImage = downloadUri.toString()
                    updateArticle()
                } else {
                    toast(this, "Upload Failed")
                }
            }?.addOnFailureListener {
                toast(this, "Upload Failed " + it)
            }
        } else {
            newImage = bunImage
            updateArticle()
        }
    }

    private fun resetForm() {
        inptTitle.setText(bunTitle)
        inptContent.setText(bunContent)

        Glide.with(applicationContext)
            .load(bunImage)
            .into(image_preview)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                image_preview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setOnClickListener() {
        delete_btn.setOnClickListener {
            deleteArticle()
        }

        submit_btn.setOnClickListener {
            uploadImage()
        }

        cancel_btn.setOnClickListener {
            resetForm()
        }

        image_pick.setOnClickListener {
            imagePicker()
        }
    }
}