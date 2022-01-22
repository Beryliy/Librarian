package com.wildhunt.librarian.data

import com.wildhunt.librarian.domain.Book
import com.wildhunt.librarian.domain.repository.BooksRepository

class BooksRemoteRepository(
  private val booksAPI: BooksAPI,
) : BooksRepository {
  override suspend fun getBooks(keywords: List<String>): List<Book> {
    return booksAPI.getBooks(
      query = keywords.joinToString(",")
    ).items.map { item -> item.toBook() }
  }
}