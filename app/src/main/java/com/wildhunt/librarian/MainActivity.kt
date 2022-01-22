package com.wildhunt.librarian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wildhunt.librarian.data.WitApi
import com.wildhunt.librarian.databinding.ActivityMainBinding
import com.wildhunt.librarian.di.AppComponent
import com.wildhunt.librarian.domain.use_cases.GetBooksUseCase
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
  private var _binding: ActivityMainBinding? = null
  private val binding: ActivityMainBinding get() = _binding!!

  @Inject
  lateinit var keywordsUseCase: GetKeywordsUseCase

  @Inject
  lateinit var booksUseCase: GetBooksUseCase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    AppComponent.get(this).inject(this)

    lifecycleScope.launchWhenCreated {
      withContext(Dispatchers.IO) {
        val keywords = keywordsUseCase.get("I don't to read anything.")
        val books = booksUseCase.getBooks(keywords.takeUnless { it.isEmpty() } ?: listOf("I don't to read anything"))

        withContext(Dispatchers.Main) {
          binding.text.text = books.joinToString()
        }
      }
    }
  }
}