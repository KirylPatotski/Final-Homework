package com.homework.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.homework.app.mvvw.data.storage.AppPreferencesImpl
import com.homework.app.mvvw.ui.auth.fragment.AuthFragment


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val NAVIGATION_EVENT = "navigation_event"
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listenNavigationEvents()

        if (savedInstanceState == null) {
            if (AppPreferencesImpl.getInstance(this).getToken().isBlank()) {
//                openFragment(MusicFragment(), tag = "AuthFragment")
//                openFragment(AuthFragment(), tag = "AuthFragment")
                openFragment(AuthFragment(), tag = "AuthFragment")
            } else {
//                openFragment(MusicFragment(), tag = "MusicFragment")
                openFragment(AuthFragment(), tag = "MusicFragment")
            }
        }

    }


    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun openFragment(fragment: Fragment, doClearBackStack: Boolean = false, tag: String? = null) {
        if (doClearBackStack) {
            clearBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    fun findFragmentByTag(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun listenNavigationEvents() {
        supportFragmentManager.setFragmentResultListener(NAVIGATION_EVENT, this) { _, bundle ->
            val navigationEvent = bundle.get(NAVIGATION_EVENT_DATA_KEY) as String
            Log.d(TAG, navigationEvent)
        }
    }
}

