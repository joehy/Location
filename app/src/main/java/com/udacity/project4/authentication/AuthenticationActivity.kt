package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var bind:ActivityAuthenticationBinding
    private val viewModel by viewModels<AuthenticationViewModel>()
    private  val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind=DataBindingUtil.setContentView(this,R.layout.activity_authentication)

        bind.loginButtonActivityAuth.visibility= View.INVISIBLE

        checkUserAuth()
    }

    private fun checkUserAuth() {
        viewModel.authenticationState.observe(this) {
            when(it){
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED->{
                    navigateToRemindersActivity()
                }
                else -> {
                    bind.loginButtonActivityAuth.visibility= View.VISIBLE
                    bind.loginButtonActivityAuth.setOnClickListener {
                        startLogInFlow()
                    }
                }
            }
        }
    }

    private fun navigateToRemindersActivity() {
        startActivity(Intent(this, RemindersActivity::class.java))
        finish()
    }
    private fun startLogInFlow() {
// Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

}
