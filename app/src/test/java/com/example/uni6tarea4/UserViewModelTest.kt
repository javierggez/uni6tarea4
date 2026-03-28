package com.example.uni6tarea4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

// Fake para no depender de Room/Retrofit en tests unitarios
class FakeUserRepository(
    private val fakeUsers: List<User> = emptyList(),
    private val shouldThrow: Boolean = false
) : UserRepository(
    object : ApiService {
        override suspend fun getUsers(accept: String) = emptyList<UserApiModel>()
        override suspend fun getUserById(id: Int, format: String) =
            UserApiModel(0, "", "", "", "", "")
        override suspend fun createPost(contentType: String, post: PostRequest) =
            PostResponse(0, "", "", 0)
    },
    object : UserDao {
        private val liveData = MutableLiveData<List<User>>(fakeUsers)
        override fun getAllUsers(): LiveData<List<User>> = liveData
        override suspend fun getUserById(userId: Int): User? = null
        override suspend fun insertAll(users: List<User>) {}
        override suspend fun insertUser(user: User) {}
        override suspend fun deleteAll() {}
        override suspend fun count(): Int = fakeUsers.size
    }
) {
    private val _users = MutableLiveData<List<User>>(fakeUsers)
    override val users: LiveData<List<User>> get() = _users

    override suspend fun fetchAndStoreUsers() {
        if (shouldThrow) throw RuntimeException("Sin conexión")
    }

    override suspend fun getUserCount(): Int = fakeUsers.size
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val sampleUsers = listOf(
        User(1, "Alice", "alice@example.com", "111-1111", "alice.com"),
        User(2, "Bob", "bob@example.com", "222-2222", "bob.com")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ---- Tests del ViewModel ----

    @Test
    fun users_LiveData_NotNull() {
        val viewModel = UserViewModel(FakeUserRepository(sampleUsers))
        assertNotNull(viewModel.users)
    }

    @Test
    fun users_LiveData_ReturnsCorrectData() {
        val viewModel = UserViewModel(FakeUserRepository(sampleUsers))
        var result: List<User>? = null
        viewModel.users.observeForever { result = it }
        assertEquals(2, result?.size)
        assertEquals("Alice", result?.get(0)?.name)
    }

    @Test
    fun fetchUsers_OnSuccess_LoadingGoesToFalse() = runTest {
        val viewModel = UserViewModel(FakeUserRepository(sampleUsers))
        viewModel.fetchUsers()
        advanceUntilIdle()

        var loadingValue: Boolean? = null
        viewModel.isLoading.observeForever { loadingValue = it }
        assertFalse(loadingValue ?: true)
    }

    @Test
    fun fetchUsers_OnSuccess_ErrorMessageIsNull() = runTest {
        val viewModel = UserViewModel(FakeUserRepository(sampleUsers))
        viewModel.fetchUsers()
        advanceUntilIdle()

        var errorMsg: String? = "initial"
        viewModel.errorMessage.observeForever { errorMsg = it }
        assertNull(errorMsg)
    }

    @Test
    fun fetchUsers_OnError_SetsErrorMessage() = runTest {
        val viewModel = UserViewModel(FakeUserRepository(shouldThrow = true))
        viewModel.fetchUsers()
        advanceUntilIdle()

        var errorMsg: String? = null
        viewModel.errorMessage.observeForever { errorMsg = it }
        assertNotNull(errorMsg)
        assertTrue(errorMsg!!.contains("Sin conexión"))
    }

    @Test
    fun getUserSummary_ReturnsFormattedString() {
        val viewModel = UserViewModel(FakeUserRepository())
        val user = User(1, "Alice", "alice@example.com", "111", "alice.com")
        val summary = viewModel.getUserSummary(user)
        assertTrue(summary.contains("Alice"))
        assertTrue(summary.contains("alice@example.com"))
    }

    // ---- Tests del modelo User ----

    @Test
    fun user_isValid_WithValidData_ReturnsTrue() {
        val user = User(1, "Alice", "alice@example.com", "111", "alice.com")
        assertTrue(user.isValid())
    }

    @Test
    fun user_isValid_WithBlankName_ReturnsFalse() {
        val user = User(1, "", "alice@example.com", "111", "alice.com")
        assertFalse(user.isValid())
    }

    @Test
    fun user_isValid_WithBlankEmail_ReturnsFalse() {
        val user = User(1, "Alice", "", "111", "alice.com")
        assertFalse(user.isValid())
    }

    @Test
    fun user_displayInfo_ContainsNameAndEmail() {
        val user = User(1, "Alice", "alice@example.com", "111", "alice.com")
        val info = user.displayInfo()
        assertTrue(info.contains("Alice"))
        assertTrue(info.contains("alice@example.com"))
    }

    // ---- Tests del mapeo API ----

    @Test
    fun userApiModel_toUser_MapsAllFieldsCorrectly() {
        val apiModel = UserApiModel(
            id = 5,
            name = "Carlos",
            username = "carlosx",
            email = "carlos@example.com",
            phone = "555-1234",
            website = "carlos.dev"
        )
        val user = apiModel.toUser()
        assertEquals(5, user.id)
        assertEquals("Carlos", user.name)
        assertEquals("carlos@example.com", user.email)
        assertEquals("555-1234", user.phone)
        assertEquals("carlos.dev", user.website)
    }

    @Test
    fun userList_Filter_OnlyValidUsersPass() {
        val users = listOf(
            User(1, "Alice", "alice@example.com", "111", "alice.com"),
            User(2, "", "bob@example.com", "222", "bob.com"),
            User(3, "Carlos", "", "333", "carlos.com")
        )
        val validUsers = users.filter { it.isValid() }
        assertEquals(1, validUsers.size)
        assertEquals("Alice", validUsers[0].name)
    }

    @Test
    fun userList_Map_ExtractsNamesCorrectly() {
        val users = sampleUsers
        val names = users.map { it.name }
        assertEquals(listOf("Alice", "Bob"), names)
    }
}

// ---- Tests con Mockito real (@Mock + verify) ----

@RunWith(MockitoJUnitRunner::class)
class UserViewModelMockitoTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepository: UserRepository

    @Test
    fun viewModel_ExposesLiveDataFromRepository() {
        val fakeUsers = listOf(
            User(1, "Ana García", "ana@ejemplo.com", "111-1111", "ana.com"),
            User(2, "Carlos López", "carlos@ejemplo.com", "222-2222", "carlos.com")
        )
        val liveData = MutableLiveData<List<User>>(fakeUsers)
        `when`(mockRepository.users).thenReturn(liveData)

        val viewModel = UserViewModel(mockRepository)
        var result: List<User>? = null
        viewModel.users.observeForever { result = it }

        assertEquals(2, result?.size)
        assertEquals("Ana García", result?.get(0)?.name)
        // Verifica que el ViewModel accedió a la propiedad users del repositorio
        verify(mockRepository).users
    }

    @Test
    fun viewModel_UsersLiveData_IsNotNullWithMock() {
        val liveData = MutableLiveData<List<User>>(emptyList())
        `when`(mockRepository.users).thenReturn(liveData)

        val viewModel = UserViewModel(mockRepository)

        assertNotNull(viewModel.users)
        verify(mockRepository).users
    }
}
