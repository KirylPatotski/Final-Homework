package com.omisoft.myapplication

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.omisoft.myapplication.MainActivity.Companion.HANDLER_DATA_KEY
import com.omisoft.myapplication.mvvm.model.storage.preferences.AppPreferencesImpl
import com.omisoft.myapplication.mvvm.ui.auth.fragment.AuthFragment
import com.omisoft.myapplication.mvvm.ui.draft.countries.fragment.AlbumsListFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val NAVIGATION_EVENT = "navigation_event"
        const val NAVIGATION_EVENT_DATA_KEY = "navigation_event_data_key"
        const val HANDLER_DATA_KEY = "handler_data_key"
    }

    private val myThread = MyCustomHandlerThread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listenNavigationEvents()

        if (savedInstanceState == null) {
            if (AppPreferencesImpl.getInstance(this).getToken().isBlank()) {
                openFragment(AuthFragment(), tag = "AuthFragment")
            } else {
                openFragment(AlbumsListFragment(), tag = "ListFragment")
            }
        }

        myThread.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val handler = myThread.getHandler()

            val message = Message()
            val bundle = Bundle()
            bundle.putString(HANDLER_DATA_KEY, "task 1")
            message.data = bundle
            handler?.sendMessage(message)
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            val handler = myThread.getHandler()

            val message = Message()
            val bundle = Bundle()
            bundle.putString(HANDLER_DATA_KEY, "task 2")
            message.data = bundle
            handler?.sendMessage(message)
        }, 4000)

        Handler(Looper.getMainLooper()).postDelayed({
            val handler = myThread.getHandler()

            val message = Message()
            val bundle = Bundle()
            bundle.putString(HANDLER_DATA_KEY, "task 3")
            message.data = bundle
            handler?.sendMessage(message)
        }, 6000)
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

    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    private fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun listenNavigationEvents() {
        supportFragmentManager.setFragmentResultListener(NAVIGATION_EVENT, this) { _, bundle ->
            val navigationEvent = bundle.get(NAVIGATION_EVENT_DATA_KEY) as String
            Log.d(TAG, navigationEvent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myThread.quitSafely()
    }
}

class MyCustomThread : Thread() {
    var myHandler: Handler? = null

    override fun run() {
        super.run()

        Looper.prepare()
        val looper = Looper.myLooper()

        looper?.let {
            myHandler = object : Handler(looper) {
                override fun handleMessage(msg: Message) {
                    val bundle = msg.data

                    when (bundle.getString(HANDLER_DATA_KEY)) {
                        "task 1" -> {
                            println("task 1")
                        }
                        "task 2" -> {
                            println("task 2")
                        }
                        "task 3" -> {
                            println("task 3")
                        }
                        else -> {
                            println("else")
                        }
                    }
                }
            }
        }

        Looper.loop()

        println("This code will be launched after thread stops")
    }

    fun getHandler(): Handler? = myHandler
}

class MyCustomHandlerThread : HandlerThread("MyCustomThread") {
    var myHandler: Handler? = null

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        myHandler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data

                when (bundle.getString(HANDLER_DATA_KEY)) {
                    "task 1" -> {
                        println("task 1")
                    }
                    "task 2" -> {
                        println("task 2")
                    }
                    "task 3" -> {
                        println("task 3")
                    }
                    else -> {
                        println("else")
                    }
                }
            }
        }

    }

    fun getHandler(): Handler? = myHandler
}