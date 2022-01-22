package com.wildhunt.librarian.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WitResponse(
    val error: String? = null,
    val code: String? = null,
    val text: String? = null,
    val intents: List<Intent>? = null,
    val entities: Map<String, List<Entity>>? = null,
) {
    data class Intent(
        val id: String,
        val name: String,
        val confidence: Double,
    )

    data class Entity(
        val id: String,
        val name: String,
        val role: String,
        val start: Int,
        val end: Int,
        val body: String,
        val value: String,
        val confidence: Double,
        val entities: List<Entity>
    )

    val isError get() = error != null && code != null
    val isSuccess get() = !isError

    fun toSuccess() = WitSuccessResponse(
        text ?: "",
        intents ?: listOf(),
        entities ?: mapOf(),
    )

    fun toError() = WitErrorResponse(error ?: "", code ?: "")
}

data class WitSuccessResponse(
    val text: String = "",
    val intents: List<WitResponse.Intent> = listOf(),
    val entities: Map<String, List<WitResponse.Entity>> = mapOf(),
)

data class WitErrorResponse(
    val error: String = "",
    val code: String = "",
)
