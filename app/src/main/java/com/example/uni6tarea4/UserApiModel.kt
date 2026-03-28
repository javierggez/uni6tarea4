package com.example.uni6tarea4

data class UserApiModel(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        email = email,
        phone = phone,
        website = website
    )
}
