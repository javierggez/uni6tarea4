package com.example.uni6tarea4

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Header("Accept") accept: String = "application/json"
    ): List<UserApiModel>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int,
        @Query("_format") format: String = "json"
    ): UserApiModel

    // POST: enviar un nuevo post al servidor
    @POST("posts")
    suspend fun createPost(
        @Header("Content-Type") contentType: String = "application/json",
        @Body post: PostRequest
    ): PostResponse
}
