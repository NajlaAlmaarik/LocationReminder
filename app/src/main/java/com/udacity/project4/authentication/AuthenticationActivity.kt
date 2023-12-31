package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding


    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO: Implement the create account and sign in using FirebaseUI,
        //  use sign in using email and sign in using Google
        binding.logInButton.setOnClickListener {
            launchSignInFlow()
        }

        // TODO: If the user was authenticated, send him to RemindersActivity
//        viewModel.authenticationState.observe(view, Observer {authenticationState ->
//            when(authenticationState){
//                LogInViewModel.AuthenticationState.AUTHENTICATED -> startActivity(Intent(this,RemindersActivity::class.java))
//                else -> {
//                    Log.e( TAG, "Unauthenticated $authenticationState" )
//                    launchSignInFlow()
//                }
//            }
//        })
        // TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout
    }

    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email or Google account.
        // If users choose to register with their email,
        // they will need to create a password as well.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

            // This is where you can provide more ways for users to register and
            // sign in.
        )

        val customLayout = AuthMethodPickerLayout.Builder(R.layout.custome_auth_layout)
            .setEmailButtonId(R.id.email_button)
            .setGoogleButtonId(R.id.google_button)
            .build()

        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(customLayout)
                .setAvailableProviders(providers)
                .build(),
            AuthenticationActivity.SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(
                    TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
                startActivity(Intent(this,RemindersActivity::class.java))
                finishAffinity()
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using the back button. Otherwise check // response.getError().getErrorCode() and handle the error.
                Toast.makeText(this, "Sign in unsuccessful ${response?.error?.errorCode}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}