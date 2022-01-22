package com.wildhunt.librarian.domain.repository

interface WitRepository {
    suspend fun getWitKeywords(message: String): List<String>
}
