package com.wildhunt.librarian.data

import com.wildhunt.librarian.data.models.WitResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface WitApi {
    @GET("/message")
    suspend fun getWitResponse(
        @Query("q") message: String,
        @Query("v") version: String = "20210928",
    ): WitResponse

    @POST("/speech")
    suspend fun postAudio(
        @Query("v") version: String = "20210928",
        @Header("Content-Type") contentType: String,
        @Body body: RequestBody
    ): WitResponse
}
