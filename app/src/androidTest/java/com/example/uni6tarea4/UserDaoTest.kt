package com.example.uni6tarea4

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAll_IncreasesCount() = runBlocking {
        val users = listOf(
            User(1, "Alice", "alice@example.com", "111", "alice.com"),
            User(2, "Bob", "bob@example.com", "222", "bob.com")
        )
        userDao.insertAll(users)
        assertEquals(2, userDao.count())
    }

    @Test
    fun insertUser_ReplacesExistingOnConflict() = runBlocking {
        val user = User(1, "Alice", "alice@example.com", "111", "alice.com")
        userDao.insertUser(user)

        val updatedUser = User(1, "Alice Actualizada", "alice2@example.com", "333", "alice2.com")
        userDao.insertUser(updatedUser)

        assertEquals(1, userDao.count())
        val result = userDao.getUserById(1)
        assertEquals("Alice Actualizada", result?.name)
    }

    @Test
    fun getUserById_ReturnsCorrectUser() = runBlocking {
        val user = User(42, "Carlos", "carlos@example.com", "999", "carlos.dev")
        userDao.insertUser(user)

        val result = userDao.getUserById(42)
        assertNotNull(result)
        assertEquals("Carlos", result!!.name)
        assertEquals("carlos@example.com", result.email)
    }

    @Test
    fun getUserById_ReturnsNull_WhenNotFound() = runBlocking {
        val result = userDao.getUserById(9999)
        assertNull(result)
    }

    @Test
    fun deleteAll_RemovesAllUsers() = runBlocking {
        userDao.insertAll(listOf(
            User(1, "Alice", "alice@example.com", "111", "alice.com"),
            User(2, "Bob", "bob@example.com", "222", "bob.com")
        ))
        userDao.deleteAll()
        assertEquals(0, userDao.count())
    }

    @Test
    fun count_StartsAtZero() = runBlocking {
        assertEquals(0, userDao.count())
    }

    @Test
    fun count_IncreasesWithEachInsert() = runBlocking {
        userDao.insertUser(User(1, "Alice", "alice@example.com", "111", "alice.com"))
        assertEquals(1, userDao.count())
        userDao.insertUser(User(2, "Bob", "bob@example.com", "222", "bob.com"))
        assertEquals(2, userDao.count())
    }

    @Test
    fun getAllUsers_LiveData_EmitsData() {
        runBlocking {
            userDao.insertAll(listOf(
                User(1, "Alice", "alice@example.com", "111", "alice.com")
            ))
        }

        val liveData = userDao.getAllUsers()
        val latch = CountDownLatch(1)
        var emitted: List<User>? = null

        liveData.observeForever(object : Observer<List<User>> {
            override fun onChanged(value: List<User>) {
                emitted = value
                latch.countDown()
                liveData.removeObserver(this)
            }
        })

        latch.await(2, TimeUnit.SECONDS)
        assertNotNull(emitted)
        assertEquals(1, emitted!!.size)
        assertEquals("Alice", emitted!![0].name)
    }
}
