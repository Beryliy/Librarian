package com.wildhunt.librarian

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.wildhunt.librarian.databinding.ActivityMainBinding
import com.wildhunt.librarian.di.AppComponent
import com.wildhunt.librarian.domain.use_cases.GetBooksUseCase
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCase
import com.wildhunt.librarian.sign_in.SignInActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
  private var _binding: ActivityMainBinding? = null
  private val binding: ActivityMainBinding get() = _binding!!

  private val signIn = registerForActivityResult(SignIn()) {
    //TODO: display UI
  }

  @Inject
  lateinit var keywordsUseCase: GetKeywordsUseCase

  @Inject
  lateinit var booksUseCase: GetBooksUseCase

  override fun onCreate(savedInstanceState: Bundle?) {
    AppComponent.get(this).inject(this)
    super.onCreate(savedInstanceState)
    checkLoggedIn()
    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
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