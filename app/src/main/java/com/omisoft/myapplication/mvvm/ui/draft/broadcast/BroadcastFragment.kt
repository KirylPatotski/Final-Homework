package com.omisoft.myapplication.mvvm.ui.draft.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.omisoft.myapplication.R
import kotlinx.parcelize.Parcelize

class BroadcastFragment : Fragment() {
    companion object {
        private const val BROADCAST_CONTENT_BUNDLE = "BROADCAST_CONTENT_BUNDLE"
        private const val BROADCAST_CONTENT = "BROADCAST_CONTENT"
        private const val BROADCAST_INTENT_FILTER_UPDATE_CONTENT = "BROADCAST_INTENT_FILTER_UPDATE_CONTENT"
    }

    private lateinit var contentText: AppCompatTextView
    private lateinit var sendBroadcastButton: AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_broadcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().registerReceiver(MyBroadcastReceiver(), IntentFilter(BROADCAST_INTENT_FILTER_UPDATE_CONTENT))

        contentText = view.findViewById(R.id.broadcast_content_text)
        sendBroadcastButton = view.findViewById(R.id.send_broadcast_button)

        sendBroadcastButton.setOnClickListener {
            requireActivity().sendBroadcast(Intent().apply {
                action = BROADCAST_INTENT_FILTER_UPDATE_CONTENT
                putExtra(BROADCAST_CONTENT_BUNDLE, Bundle().apply {
                    putParcelable(BROADCAST_CONTENT, User("Ivan", "Petrov", 20))
                })
            })
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val extras = intent?.getBundleExtra(BROADCAST_CONTENT_BUNDLE)
            val user = extras?.getParcelable<User>(BROADCAST_CONTENT)
            user?.let { nonNullUser ->
                val value = "Name: ${nonNullUser.name}, surname: ${nonNullUser.surname}, age: ${nonNullUser.age}"
                contentText.text = value
            }
        }
    }
}

@Parcelize
data class User(val name: String, val surname: String, val age: Int) : Parcelable