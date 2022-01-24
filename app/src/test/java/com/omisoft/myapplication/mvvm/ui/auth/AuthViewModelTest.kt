package com.omisoft.myapplication.mvvm.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthService
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthServiceImpl
import com.omisoft.myapplication.mvvm.fakes.FakeAppPreferences
import com.omisoft.myapplication.mvvm.fakes.FakeUserStorage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun prepareAuthViewModel() {
        authViewModel = AuthViewModel(
            NetworkAuthServiceImpl() as NetworkAuthService,
            FakeUserStorage(),
            FakeAppPreferences(),
        )
    }

    @Test
    fun check_login_if_password_wrong() {
        authViewModel.onLoginClicked("yura@d.com", "123")
        val isLoginSuccessLiveData = authViewModel.isLoginSuccessLiveData.value
        val isLoginFailedLiveData = authViewModel.isLoginFailedLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailedLiveData)
    }

    @Test
    fun check_login_if_email_wrong() {
        authViewModel.onLoginClicked("yurad.com", "123456")
        val isLoginSuccessLiveData = authViewModel.isLoginSuccessLiveData.value
        val isLoginFailedLiveData = authViewModel.isLoginFailedLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailedLiveData)
    }

    @Test
    fun check_login_if_email_empty() {
        authViewModel.onLoginClicked("", "123456")
        val isLoginSuccessLiveData = authViewModel.isLoginSuccessLiveData.value
        val isLoginFailedLiveData = authViewModel.isLoginFailedLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailedLiveData)
    }

    @Test
    fun check_login_if_password_empty() {
        authViewModel.onLoginClicked("yura@d.com", "")
        val isLoginSuccessLiveData = authViewModel.isLoginSuccessLiveData.value
        val isLoginFailedLiveData = authViewModel.isLoginFailedLiveData.value

        assertEquals(null, isLoginSuccessLiveData)
        assertEquals(true, isLoginFailedLiveData)
    }

    @Test
    fun check_login_if_credentials_right() {
        authViewModel.onLoginClicked("yura@d.com", "123456")
        val isLoginSuccessLiveData = authViewModel.isLoginSuccessLiveData.value
        val isLoginFailedLiveData = authViewModel.isLoginFailedLiveData.value

        assertEquals(true, isLoginSuccessLiveData)
        assertEquals(null, isLoginFailedLiveData)
    }
}