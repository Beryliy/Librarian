package com.wildhunt.librarian.domain.use_cases

import com.wildhunt.librarian.domain.models.Book
import com.wildhunt.librarian.domain.repository.BooksRepository

interface GetBooksUseCase {
    suspend fun getBooks(keywords: List<String>): List<Book>
}

class GetBooksUseCaseImpl(
    private val repo: BooksRepository
) : GetBooksUseCase {
    override suspend fun getBooks(keywords: List<String>): List<Book> {
        return repo.getBooks(keywords)
    }
}
