package io.github.kotstruct.example

import java.time.Instant
import java.util.UUID

data class ProductModel(
    val id: UUID,
    val name: String,
    val price: Int,
    val updatedAt: Instant,
    val createdAt: Instant
)
