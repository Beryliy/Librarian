package com.wildhunt.librarian.domain

class Book (
  val id: String,
  val authors: List<String>,
  val categories: List<String>,
  val description: String,
  val imageLinks: Images,
  val language: String,
  val pageCount: Int,
  val subtitle: String,
  val title: String
)