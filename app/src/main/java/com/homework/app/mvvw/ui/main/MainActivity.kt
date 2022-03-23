package com.homework.app.mvvw.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homework.app.R
import com.homework.app.mvvw.AppPreferencesImpl
import com.homework.app.mvvw.ui.login.LoginFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    companion object {
        const val NAVIGATION_EVENT = "navigation_event"
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        listenNavigationEvents()

        if (savedInstanceState == null) {
            if (AppPreferencesImpl.getInstance(this).getToken().isBlank()) {
                openFragment(LoginFragment(), tag = "Fragment")
            } else {
                openFragment(LoginFragment(), tag = "Fragment")
            }
        }

    }


    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            exitProcess(0)
        }
    }


    fun openFragment(fragment: Fragment, doClearBackStack: Boolean = false, tag: String? = null) {
        if (doClearBackStack) {
            clearBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.alpha,R.anim.alpha_out)
            .replace(R.id.main_fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun listenNavigationEvents() {
        supportFragmentManager.setFragmentResultListener(NAVIGATION_EVENT, this) { _, bundle ->
            val navigationEvent = bundle.get(NAVIGATION_EVENT_DATA_KEY) as String
        }
    }
}

