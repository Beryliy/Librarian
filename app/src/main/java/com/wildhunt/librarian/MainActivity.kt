package com.wildhunt.librarian

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.wildhunt.librarian.sign_in.SignInActivity

class MainActivity : AppCompatActivity() {
  private val signIn = registerForActivityResult(SignIn()) {
    //TODO: display UI
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    checkLoggedIn()
    setContentView(R.layout.activity_main)
  }

  private fun checkLoggedIn() {
    if(GoogleSignIn.getLastSignedInAccount(this) == null)
      signIn.launch(Intent(this, SignInActivity::class.java))
  }

  inner class SignIn : ActivityResultContract<Intent, Unit>() {

    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?) {}
  }
}