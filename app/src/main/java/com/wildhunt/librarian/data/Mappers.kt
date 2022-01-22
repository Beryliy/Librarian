package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.ImageLinks
import com.wildhunt.librarian.data.models.Item
import com.wildhunt.librarian.domain.Book
import com.wildhunt.librarian.domain.Images

fun ImageLinks.toImages(): Images =
  Images(smallThumbnail, thumbnail)

fun Item.toBook(): Book =
  volumeInfo.run {
    Book(
      id,
      authors ?: listOf(),
      categories ?: listOf(),
      description ?: "",
      imageLinks?.toImages(),
      language ?: "",
      pageCount ?: 0,
      subtitle ?: "",
      title ?: ""
    )
  }