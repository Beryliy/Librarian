package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.BooksResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BooksAPI {

  @GET("/books/v1/volumes")
  suspend fun getBooks(
    @Query("q") query: String
  ): BooksResponse
}