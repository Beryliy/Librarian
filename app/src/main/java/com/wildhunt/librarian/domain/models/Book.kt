package com.wildhunt.librarian.domain.models

data class Book (
  val id: String,
  val authors: List<String>,
  val categories: List<String>,
  val description: String,
  val imageLinks: Images? = null,
  val rating: Double = 0.0,
  val language: String,
  val pageCount: Int,
  val subtitle: String,
  val title: String
)