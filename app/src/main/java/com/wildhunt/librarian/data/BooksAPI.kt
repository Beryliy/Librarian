package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.BooksResponse
import com.wildhunt.librarian.data.models.VolumeInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksAPI {

  @GET("/books/v1/volumes")
  suspend fun getBooks(
    @Query("q") query: String
  ): BooksResponse

  @GET("/books/v1/volumes/{id}")
  suspend fun getDetails(@Path("id") id: String): VolumeInfo
}