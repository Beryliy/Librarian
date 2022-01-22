package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeInfo (
  val authors: List<String>? = null,
  val categories: List<String>? = null,
  val description: String? = null,
  val imageLinks: ImageLinks? = null,
  val language: String? = null,
  val pageCount: Int? = null,
  val subtitle: String? = null,
  val title: String? = null,
)