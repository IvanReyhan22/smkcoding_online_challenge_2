package com.ezyindustries.conews.Auth_Screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ezyindustries.conews.Adapter.mData
import com.ezyindustries.conews.Data.UserModel
import com.ezyindustries.conews.Data.UserData
import com.ezyindustries.conews.MainActivity
import com.ezyindustries.conews.R
import com.ezyindustries.conews.Retrofit.apiRequest
import com.ezyindustries.conews.Retrofit.httpClient
import com.ezyindustries.conews.Service.AuthService
import com.ezyindustries.conews.Util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    val RC_SIGN_IN: Int = 1
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        ref = FirebaseDatabase.getInstance().getReference("User")

        configureGoogleSignIn()

        isLoggedIn()
        onClick()

    }

    private fun isLoggedIn() {

//    ==========================================NODEJS API==================================================
//        val sharedPreferences = mData(applicationContext)
//
//        val user_id = sharedPreferences.getString("USER_ID")
//
//        if (user_id != "") {
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }
//    ==========================================NODEJS API==================================================

        val user = firebaseAuth.currentUser

        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun onClick() {
        register_btn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        login_btn.setOnClickListener {

            when {
                inpt_email.text.isEmpty() -> inpt_email.error = "Require"
                inpt_password.text.isEmpty() -> inpt_password.error = "Require"
                !inpt_email.text.toString().trim()
                    .matches(emailPattern.toRegex()) -> inpt_email.error = "Not Valid Email"

                else -> loginHandler()
            }

        }

        // Login with google
        google_button.setOnClickListener {
            loginWithGoogle()
        }
    }

    private fun loginHandler() {

        val intent = Intent(this, MainActivity::class.java)

        val httpClient = httpClient()
        val apiRequest = apiRequest<AuthService>(httpClient)

        val login = apiRequest.doLogin(
            inpt_email.text.toString(),
            inpt_password.text.toString()
        )

        login.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                toast(applicationContext, "FAILED SERVER CLOSED" + t.message)
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {

                if (response.body()!!.userId > 0) {

                    val data = mData(applicationContext)

                    data.setString("USER_ID", response.body()!!.userId.toString())
                    data.setString("USERNAME", response.body()!!.username)
                    data.setString("USER_EMAIL", response.body()!!.email)
                    data.setString("USER_PHONE", response.body()!!.password)

                    startActivity(intent)
                    finish()

                } else if (response.body()!!.status.equals("false")) {

                    toast(applicationContext, response.body()!!.values)

                } else {

                    toast(applicationContext, "No Internet or Server Down")

                }

            }

        })

    }

    private fun loginWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignIn() {
//        37373210738-i5s5fmvi91osu9697m2dsllt2pg4etib.apps.googleusercontent.com
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("ERROR", "google sign in error" + e)
                toast(this, "Google sign in fail")
            }
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {
                val acct = GoogleSignIn.getLastSignedInAccount(this)

                //Insert Data in database
                val userId = firebaseAuth.currentUser?.uid.toString()

                val data = UserModel(
                    userId,
                    acct?.photoUrl.toString(),
                    acct?.displayName.toString(),
                    acct?.email.toString(),
                    ""
                )

                val sharedPreferences = mData(applicationContext)

                sharedPreferences.setString("USER_ID", userId)
                sharedPreferences.setString("USERNAME", acct?.displayName.toString())
                sharedPreferences.setString("USER_EMAIL", acct?.email.toString())
                sharedPreferences.setString("USER_PHOTO", acct?.photoUrl.toString())

                ref.child(userId).setValue(data).addOnCompleteListener {
                    toast(this, "Welcome" + acct?.displayName)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun getProfileInformation() {
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        if (acct != null) {
            toast(this, acct.displayName.toString())

        } else {
            firebaseAuth.signOut()
            toast(this, "Google sign in failed, please retry")
        }
    }


}
