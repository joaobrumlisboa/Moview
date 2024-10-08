package com.example.moview

import com.example.moview.data.User
import com.example.moview.data.UserDao
import com.example.moview.viewmodel.UserViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private lateinit var userDao: UserDao
    private lateinit var userViewModel: UserViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userDao = mock(UserDao::class.java)
        userViewModel = UserViewModel(userDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test user registration success`() = runTest {
        val username = "teste"
        val password = "senha"
        val newUser = User(nome = username, senha = password)

        `when`(userDao.getUserByName(username)).thenReturn(null)
        doNothing().`when`(userDao).insert(newUser)

        var onSuccessCalled = false
        var onErrorCalled = false

        userViewModel.register(username, password, {
            onSuccessCalled = true
        }, {
            onErrorCalled = true
        })

        argumentCaptor<User>().apply {
            verify(userDao).insert(capture())
            assertEquals(username, firstValue.nome)
            assertEquals(password, firstValue.senha)
        }

        assert(onSuccessCalled)
        assert(!onErrorCalled)
    }

    @Test
    fun `test login failure wrong password`() = runTest {
        val username = "teste"
        val correctPassword = "senhacerta"
        val wrongPassword = "senhaerrada"
        val existingUser = User(nome = username, senha = correctPassword)

        `when`(userDao.getUserByName(username)).thenReturn(existingUser)

        var loginUser: User? = null

        userViewModel.login(username, wrongPassword) {
            loginUser = it
        }

        assertEquals(null, loginUser)
    }
}
