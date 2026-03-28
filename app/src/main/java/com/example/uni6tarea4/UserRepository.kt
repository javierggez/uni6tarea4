package com.example.uni6tarea4

import androidx.lifecycle.LiveData

open class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {

    open val users: LiveData<List<User>> = userDao.getAllUsers()

    open suspend fun fetchAndStoreUsers() {
        val apiModels = apiService.getUsers()
        
        val latinoNames = listOf(
            "Juan Pérez", 
            "María García", 
            "Carlos Rodríguez", 
            "Ana Martínez", 
            "Luis Hernández", 
            "Elena López", 
            "José González", 
            "Lucía Sánchez", 
            "Pedro Ramírez", 
            "Sofía Torres"
        )

        // map: convierte modelos de API a entidades Room con nombres latinos
        val users = apiModels.map { apiModel ->
            val user = apiModel.toUser()
            // Reemplazamos el nombre original por uno de nuestra lista latina
            val index = (user.id - 1) % latinoNames.size
            val latinoName = latinoNames[if (index >= 0) index else 0]
            user.copy(name = latinoName)
        }

        // filter: solo usuarios con datos válidos
        val validUsers = users.filter { it.isValid() }
        userDao.deleteAll()
        userDao.insertAll(validUsers)
    }

    open suspend fun getUserCount(): Int = userDao.count()

    suspend fun addSampleUser() {
        val sample = User(
            id = 999,
            name = "Miguel Ángel",
            email = "miguel@ejemplo.com",
            phone = "000-0000",
            website = "miguel.com"
        ).apply {
            // apply: configuración inline del objeto
        }
        userDao.insertUser(sample)
    }
}
