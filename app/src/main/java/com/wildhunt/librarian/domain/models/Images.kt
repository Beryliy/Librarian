package com.wildhunt.librarian.domain.models

data class Images(
  val smallThumbnail: String,
  val thumbnail: String,
  val medium: String?,
  val large: String?,
)