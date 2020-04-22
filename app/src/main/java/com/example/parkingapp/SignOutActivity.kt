package com.example.parkingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_sign_out.*

class SignOutActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_out)

        setupUI()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            updateUI(account)
        }
    }

    private fun updateUI(account : GoogleSignInAccount) {
        textView.text = account.displayName
        textView2.text =account.email
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()

        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signOut() {
        configureGoogleSignIn()
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {

            }

        startActivity(SignInActivity.getLaunchIntent(this))
    }
}
