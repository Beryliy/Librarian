package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item (
  val id: String,
  val volumeInfo: VolumeInfo
)