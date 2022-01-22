package com.wildhunt.librarian.data

import android.content.SharedPreferences
import com.wildhunt.librarian.domain.Book

class BooksRepo(
  private val booksAPI: BooksAPI,
  private val preferences: SharedPreferences
) {

  suspend fun getBooks(query: String, subject: String): List<Book> =
    booksAPI.getBooks(
      query = query + "subject:" + subject,""//TODO: place actual key
    ).items.map { item -> item.toBook() }

}