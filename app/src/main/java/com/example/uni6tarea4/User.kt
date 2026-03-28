package com.example.uni6tarea4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val website: String
) {
    fun displayInfo(): String = "Nombre: $name | Email: $email"

    fun isValid(): Boolean = name.isNotBlank() && email.isNotBlank()
}
