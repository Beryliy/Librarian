package com.wildhunt.librarian.sign_in

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.wildhunt.librarian.databinding.ActivitySignInBinding
import com.wildhunt.librarian.sign_in.di.SignInComponent
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
  private lateinit var viewModel: SignInViewModel

  @Inject lateinit var signInIntent: Intent
  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var binding: ActivitySignInBinding

  private val signIn = registerForActivityResult(SignIn()) { task ->
    try {
      viewModel.userSignedIn(task.result.idToken)
    } catch (ae: ApiException) {
      //TODO: handle error
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    SignInComponent.get(applicationContext).inject(this)
    super.onCreate(savedInstanceState)
    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)
    viewModel = ViewModelProvider(this, viewModelFactory)[SignInViewModel::class.java]
    setupViews()
  }

  private fun setupViews() {
    binding.signIn.setOnClickListener {
      startSignInFlow()
    }
  }

  private fun startSignInFlow() = signIn.launch(signInIntent)

  class SignIn : ActivityResultContract<Intent, Task<GoogleSignInAccount>>() {
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount> =
      GoogleSignIn.getSignedInAccountFromIntent(intent)
  }

  companion object {
    private const val SIGN_IN = 1
  }
}