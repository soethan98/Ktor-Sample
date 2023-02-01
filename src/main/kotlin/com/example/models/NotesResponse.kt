package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NotesResponse<T>(
    val success:Boolean,
    val data:T
)