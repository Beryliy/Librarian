package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.WitResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WitApi {
    @GET("/message")
    suspend fun getWitResponse(
        @Query("q") message: String,
        @Query("v") version: String = "20210928",
    ): WitResponse
}
