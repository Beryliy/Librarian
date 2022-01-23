package com.wildhunt.librarian.data

import com.wildhunt.librarian.domain.repository.WitRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class WitRepositoryImpl(
    private val witApi: WitApi,
) : WitRepository {
    override suspend fun getWitKeywords(message: String): List<String> {
        val response = witApi.getWitResponse(message)
        return response.toSuccess().entities.flatMap { (_, v) -> v.map { it.value } }
    }

    override suspend fun uploadAudio(file: File): List<String> {
        val body = file.readBytes().let {
            it.toRequestBody("application/octet".toMediaTypeOrNull(), 0, it.size)
        }

        val response = witApi.postAudio(contentType = "audio/mpeg3", body = body)
        return response.toSuccess().entities.flatMap { (_, v) -> v.map { it.value } }
    }
}
