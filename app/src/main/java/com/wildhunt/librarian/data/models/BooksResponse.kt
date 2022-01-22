package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksResponse(
  val items: List<Item>,
  val totalItems: Int
)