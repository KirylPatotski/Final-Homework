package com.homework.app.mvvw.ui.browser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.homework.app.R
import com.homework.app.mvvw.ui.music.fragment.Adapter
import kotlin.system.exitProcess


class BrowserFragment() : Fragment() {

    private lateinit var webView: WebView
    private lateinit var urlField: TextView
    private lateinit var urlButton: AppCompatButton
    private lateinit var exitButtonWeb: AppCompatButton
    private lateinit var str: String


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        webView = view.findViewById(R.id.web_view)
        urlField = view.findViewById(R.id.textView_url_fragment)
        exitButtonWeb = view.findViewById(R.id.button_exit_browser)
        urlButton = view.findViewById(R.id.button_view_url)


        var favoriteUrl = Adapter.ViewHolder.favorite
        println(favoriteUrl)
        favoriteUrl.replace(" ", "+", true)

        println(favoriteUrl)

        str = "https://www.last.fm/music/$favoriteUrl"

        urlField.text = str

        setListeners()
        webView.loadUrl(str)

    }

    private fun setListeners() {

        exitButtonWeb.setOnClickListener {
            exitProcess(0)
        }
        urlButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
            startActivity(browserIntent)
        }

    }
}






