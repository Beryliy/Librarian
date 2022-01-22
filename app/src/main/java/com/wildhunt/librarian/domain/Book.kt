package com.wildhunt.librarian.domain

data class Book (
  val id: String,
  val authors: List<String>,
  val categories: List<String>,
  val description: String,
  val imageLinks: Images? = null,
  val language: String,
  val pageCount: Int,
  val subtitle: String,
  val title: String
)