package com.wildhunt.librarian.data

import com.wildhunt.librarian.domain.models.Book
import com.wildhunt.librarian.domain.models.Images
import com.wildhunt.librarian.domain.repository.BooksRepository
import java.lang.Exception

class BooksRemoteRepository(
  private val booksAPI: BooksAPI,
) : BooksRepository {
  override suspend fun getBooks(keywords: List<String>): List<Book> {
    return booksAPI.getBooks(
      query = keywords.joinToString(",")
    ).items.map { item -> item.toBook() }
  }

  override suspend fun getDetails(book: Book): Book {
    return try {
      val detailed = booksAPI.getDetails(book.id)

      Book(
        id = book.id,
        authors = detailed.authors ?: book.authors,
        categories = detailed.categories ?: book.categories,
        description = detailed.description ?: book.description,
        imageLinks = Images(
          smallThumbnail = detailed.imageLinks?.smallThumbnail ?: book.imageLinks?.smallThumbnail ?: "",
          thumbnail = detailed.imageLinks?.thumbnail ?: book.imageLinks?.thumbnail ?: "",
          medium = detailed.imageLinks?.medium ?: book.imageLinks?.medium ?: "",
          large = detailed.imageLinks?.large ?: book.imageLinks?.large ?: "",
        ),
        language = detailed.language ?: book.language,
        pageCount = detailed.pageCount ?: book.pageCount,
        subtitle = detailed.subtitle ?: book.subtitle,
        title = detailed.title ?: book.title,
      )
    } catch (e: Exception) {
      e.printStackTrace()
      book
    }
  }
}