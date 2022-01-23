package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageLinks(
  val smallThumbnail: String,
  val thumbnail: String,
  val medium: String? = null,
  val large: String? = null,
)