package com.ezyindustries.conews

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Util.InternetCheck
import com.ezyindustries.conews.Util.toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_write_article.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class WriteArticleFragment : Fragment() {

    //IMAGE REFERENCE
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private var isInternetOk = false

    private val random = Random()

    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_article, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Article")
        storageReference = FirebaseStorage.getInstance().reference

        checkConnection()

        setOnClick()

    }

    private fun checkConnection() {
        /**
         * Basically if there is an internet connection it will use apiGetArticle() method to get data from database
         * but if there's no internet connection then it will load data from room local database to show data list
         */
        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (internet!!) {
                    isInternetOk = true
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClick() {

        submit_btn.setOnClickListener {
            if (isInternetOk) {
                uploadImage()
            } else {
                toast(requireContext(), "Maaf kak kamu butuh koneksi internet untuk update profil")
            }
        }

        cancel_btn.setOnClickListener {
            resetForm()
        }

        image_pick.setOnClickListener {
            imagePicker()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                    publishArticle(downloadUri.toString())
                } else {
                    toast(requireContext(), "Upload Failed")
                }
            }?.addOnFailureListener {
                toast(requireContext(), "Upload Failed " + it)
            }
        } else {
            toast(requireContext(), "Please Pick the image")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun publishArticle(imageUrl: String) {
        val inptTitle = title.text.toString()
        val inptContent = content.text.toString()

        val dateTime = LocalDateTime.now()
        val formattedDate = dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

        val user = firebaseAuth.currentUser
        val articleId = ref.push().key.toString()

        val data = ArticleModel(
            articleId,
            user!!.uid,
            inptTitle,
            inptContent,
            imageUrl,
            "daily",
            formattedDate,
            ""
        )

        if (inptTitle.isEmpty() && inptContent.isEmpty()) {
            toast(requireContext(), "Data kosong")
        } else {

            ref.child(articleId).setValue(data).addOnCompleteListener {

                sendMessageUpStream(inptTitle)
                toast(requireContext(), "Succes post article")
                resetForm()

            }

        }
    }

    private fun imagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun resetForm() {
        image_preview.setImageResource(android.R.color.transparent)
        title.setText("")
        content.setText("")
    }

    private fun sendMessageUpStream(message:String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(requireContext(),channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Berita terbaru tentang COVID-19")
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Chanel human readable title",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0,notificationBuilder.build())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePath)
                image_preview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}