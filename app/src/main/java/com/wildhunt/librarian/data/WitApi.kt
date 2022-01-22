package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.WitResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface WitApi {
    @POST("/message")
    suspend fun getWitResponse(@Query("q") message: String): WitResponse
}
