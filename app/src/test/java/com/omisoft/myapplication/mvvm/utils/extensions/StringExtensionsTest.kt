package com.omisoft.myapplication.mvvm.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun test_is_password_valid_if_empty() {
        val password = ""
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    @Test
    fun test_is_password_valid_if_null() {
        val password = null
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    @Test
    fun test_is_password_valid_if_blank() {
        val password = "        "
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    @Test
    fun test_is_password_valid_if_five_chars() {
        val password = "12345"
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    @Test
    fun test_is_password_valid_if_more_then_five_chars() {
        val password = "123456"
        val isPasswordValid = password.isPasswordValid()

        assertEquals(true, isPasswordValid)
    }

    @Test
    fun test_is_email_valid_if_null() {
        val email = null
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    @Test
    fun test_is_email_valid_if_blank() {
        val email = "        "
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    @Test
    fun test_is_email_valid_if_empty() {
        val email = ""
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    @Test
    fun test_is_email_valid_if_not_email() {
        val email = "yuriyfutysh"
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    @Test
    fun test_is_email_valid_if_email_correct() {
        val email = "yuriy@futysh.com"
        val isEmailValid = email.isEmailValid()

        assertEquals(true, isEmailValid)
    }
}