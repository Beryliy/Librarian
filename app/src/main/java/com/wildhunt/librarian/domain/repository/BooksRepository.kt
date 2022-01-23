package com.wildhunt.librarian.domain.repository

import com.wildhunt.librarian.domain.models.Book

interface BooksRepository {
    suspend fun getBooks(keywords: List<String>): List<Book>
}
