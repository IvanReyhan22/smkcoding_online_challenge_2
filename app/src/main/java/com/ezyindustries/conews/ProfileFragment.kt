package com.ezyindustries.conews


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ezyindustries.conews.Adapter.ArticleAdapter
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Auth_Screen.Login
import com.ezyindustries.conews.Data.ArticleModel
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.Data.UserModel
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var articleData: ArrayList<ArticleModel>
    private lateinit var firebaseAuth: FirebaseAuth

    private var mUserId: String = ""
    private var mName: String = ""
    private var mEmail: String = ""
    private var mCaption: String = ""
    private var mImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        getProfileData()
        getArticleData()
        onClick()
    }

    private fun onClick() {

        logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut();

//            val data = mData(context!!)
//
//            data.removeString("USER_ID")
//            data.removeString("USERNAME")
//            data.removeString("USER_EMAIL")
//            data.removeString("USER_PHONE")

            startActivity(Intent(context, Login::class.java))
            activity!!.finish()
        }

        edit.setOnClickListener {
            val intent = Intent(context, ProfileEdit::class.java)

            val bundle = Bundle()

            bundle.putString("USERID", mUserId)
            bundle.putString("NAME", mName)
            bundle.putString("IMAGE", mImage)
            bundle.putString("EMAIL", mEmail)
            bundle.putString("CAPTION", mCaption)

            intent.putExtras(bundle)

            startActivity(intent)
        }
    }

    private fun getProfileData() {

        val acct = GoogleSignIn.getLastSignedInAccount(context!!)

        if (acct != null) {

            auth = FirebaseAuth.getInstance()
            val userId: String = auth.currentUser!!.uid
            ref = FirebaseDatabase.getInstance().getReference("User")

            ref.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    toast(context!!, "Connection Error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(UserModel::class.java)

                    mUserId = userId
                    mName = data?.name.toString()
                    mEmail = data?.email.toString()
                    mImage = data?.image.toString()
                    mCaption = data?.caption.toString()

                    name.text = mName
                    email.text = mEmail
                    Glide.with(context!!)
                        .load(mImage)
                        .into(user_pic)
                    if (mCaption != "") {
                        caption.text = mCaption
                    }


                }

            })

        } else {
            toast(context!!, "Failed retrieve personal data, Please re-login")

            firebaseAuth.signOut()

            startActivity(Intent(context, Login::class.java))
            activity!!.finish()
        }

//    ==========================================NODEJS API==================================================
//        val sharedPreferences = mData(context!!)
//
//        val user_id = sharedPreferences.getString("USER_ID")
//
//        val httpClient = httpClient()
//        val apiRequest = apiRequest<AuthService>(httpClient)
//
//        val getProfile = apiRequest.getUserById(
//            user_id
//        )
//
//        getProfile.enqueue(object: Callback<UserData> {
//            override fun onFailure(call: Call<UserData>, t: Throwable) {
//                toast(context!!,"FAILED SERVER CLOSED" + t.message)
//            }
//
//            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//
//                if ( response.body()!!.userId > 0) {
//
//                    val item = response.body()!!
//
//                    name.text = item.username
//                    email.text = item.email
//
//                    if (item.caption != null) {
//                        caption.text = item.caption
//                    }
//
//
//                } else {
//                    toast(context!!,"PLEASE RESTART")
//                }
//            }
//
//        })
//    ==========================================NODEJS API==================================================
    }

    private fun getArticleData() {

        auth = FirebaseAuth.getInstance()
        val userId: String = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("Article")

        ref.orderByChild("ownerId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    toast(context!!, "Connection Error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    articleData = java.util.ArrayList<ArticleModel>()
                    for (snap in snapshot.children) {
                        val data = snap.getValue(ArticleModel::class.java)

                        articleData.add(data!!)
                    }

                    rv_myArticle.layoutManager = LinearLayoutManager(context)
                    rv_myArticle.adapter = ArticleAdapter(context!!, articleData, "vertical") {
                        val article = it

                        val intent = Intent(context, EditArticle::class.java)

                        val bundle = Bundle()

                        bundle.putString("ARTICLE_ID", article.articleId)
                        bundle.putString("OWNER_ID", article.ownerId)
                        bundle.putString("TITLE", article.title)
                        bundle.putString("CONTENT", article.content)
                        bundle.putString("IMAGE", article.image)
                        bundle.putString("DATE", article.date)

                        intent.putExtras(bundle)

                        startActivity(intent)
                    }

                }

            })


    }
}