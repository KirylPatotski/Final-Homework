package com.omisoft.myapplication.mvvm.ui.draft.browser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R

class BrowserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_browser, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        val fadeBrowser = inflater.inflateTransition(R.transition.fade_browser)
        val slideBrowser = inflater.inflateTransition(R.transition.slide_browser)

        enterTransition = slideBrowser
        exitTransition = fadeBrowser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonViewUrl = view.findViewById<AppCompatButton>(R.id.button_view_url)

        requireActivity().supportFragmentManager.setFragmentResult(
            MainActivity.NAVIGATION_EVENT,
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "BrowserFragment Created")
        )

        buttonViewUrl.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.ua/"))
            startActivity(browserIntent)
        }
    }
}