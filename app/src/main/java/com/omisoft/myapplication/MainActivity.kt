package com.omisoft.myapplication

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.work.*
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
import com.omisoft.myapplication.MainActivity.Companion.HANDLER_DATA_KEY
import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferencesImpl
import com.omisoft.myapplication.mvvm.ui.draft.weather.WeatherFragment
import com.omisoft.myapplication.mvvm.utils.CachingArtistsWorker
import com.omisoft.myapplication.mvvm.utils.PeriodicWorker
import com.omisoft.myapplication.mvvm.utils.WeatherWidget
import java.util.concurrent.TimeUnit


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
                openFragment(WeatherFragment(), tag = "AuthFragment")
            } else {
                openFragment(WeatherFragment(), tag = "MusicFragment")
            }
        }

        myThread.start()
        registerWorkManagers()
        updateProgrammaticallyHomeWidget()
//        registerThreads()
    }

    private fun updateProgrammaticallyHomeWidget() {
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, WeatherWidget::class.java).apply {
                val ids: IntArray = AppWidgetManager.getInstance(application)
                    .getAppWidgetIds(ComponentName(application, WeatherWidget::class.java))
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            sendBroadcast(intent)
        }, 5000)
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
        myThread.quitSafely()
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

    private fun registerThreads() {
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

    private fun registerWorkManagers() {
        val oneTimeConstraints: Constraints = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build()
        } else {
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build()
        }

        val worker = OneTimeWorkRequestBuilder<CachingArtistsWorker>()
            .addTag("CachingArtistsWorker1")
            .setConstraints(oneTimeConstraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        val worker2 = OneTimeWorkRequestBuilder<CachingArtistsWorker>()
            .addTag("CachingArtistsWorker2")
            .setConstraints(oneTimeConstraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        val worker3 = OneTimeWorkRequestBuilder<CachingArtistsWorker>()
            .addTag("CachingArtistsWorker3")
            .setConstraints(oneTimeConstraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(this)
            .beginWith(worker)
            .then(listOf(worker2, worker3))
            .enqueue()

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker2.id).observe(this, { workInfo ->
            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            }
        })

        val periodicConstraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val periodicWorker = PeriodicWorkRequestBuilder<PeriodicWorker>(MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
            .addTag("PeriodicWorker")
            .setConstraints(periodicConstraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorker)
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