// Pet.kt
package com.example.dog

data class Pet(
    val id: String,
    val name: String,
    val breed: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val status: PetStatus // Use the defined Enum
)

enum class PetStatus {
    LOST, FOUND
}