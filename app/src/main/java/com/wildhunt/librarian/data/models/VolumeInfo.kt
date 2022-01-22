package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeInfo (
  val authors: List<String>,
  val categories: List<String>,
  val description: String,
  val imageLinks: ImageLinks,
  val language: String,
  val pageCount: Int,
  val subtitle: String,
  val title: String
)