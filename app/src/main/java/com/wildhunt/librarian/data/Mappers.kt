package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.ImageLinks
import com.wildhunt.librarian.data.models.Item
import com.wildhunt.librarian.domain.models.Book
import com.wildhunt.librarian.domain.models.Images

fun ImageLinks.toImages(): Images = Images(smallThumbnail, thumbnail, medium, large)

fun Item.toBook(): Book =
  volumeInfo.run {
    Book(
      id ?: this@toBook.id,
      authors ?: listOf(),
      categories ?: listOf(),
      description ?: "",
      imageLinks?.toImages(),
      averageRating,
      language ?: "",
      pageCount ?: 0,
      subtitle ?: "",
      title ?: ""
    )
  }