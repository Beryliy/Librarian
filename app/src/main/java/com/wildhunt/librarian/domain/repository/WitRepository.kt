package com.wildhunt.librarian.domain.repository

import java.io.File

interface WitRepository {
    suspend fun getWitKeywords(message: String): List<String>
    suspend fun uploadAudio(file: File): List<String>
}
