package com.omisoft.myapplication.mvvm.ui.draft.countries.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.ui.draft.browser.BrowserFragment
import com.omisoft.myapplication.mvvm.ui.draft.countries.ListViewModel
import com.omisoft.myapplication.mvvm.ui.draft.filepicker.FilePickerFragment
import com.omisoft.myapplication.success.SuccessFragment


class ListFragment : Fragment() {

    companion object {
        private const val TAG = "ListFragment"
    }

    private val viewModel by viewModels<ListViewModel>()
    private lateinit var albumsRecyclerView: RecyclerView
    private var adapter: AlbumRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        val fadeBrowser = inflater.inflateTransition(R.transition.fade_browser)
        val slideBrowser = inflater.inflateTransition(R.transition.slide_browser)

        enterTransition = slideBrowser
        exitTransition = fadeBrowser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.setFragmentResult(
            MainActivity.NAVIGATION_EVENT,
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "CountriesFragment Created")
        )

        lifecycle.addObserver(viewModel)
        albumsRecyclerView = view.findViewById(R.id.list_albums)
        albumsRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true).apply {
                stackFromEnd = true
            }
        }

        val openSuccessButton = view.findViewById<AppCompatButton>(R.id.open_success_button)
        val openUrlButton = view.findViewById<AppCompatButton>(R.id.open_url_button)
        val countriesOpenFilePicker = view.findViewById<AppCompatButton>(R.id.open_file_picker_button)
        val spinner = view.findViewById<AppCompatSpinner>(R.id.spinner)
        val imageFlag = view.findViewById<AppCompatImageView>(R.id.image_flag)

        val countriesArray = resources.getStringArray(R.array.countries)

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.countries, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val country = countriesArray[position]
                Log.d(TAG, country)
                when (country) {
                    "Belarus" -> {
                        imageFlag.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.belarus))
                    }
                    "Ukraine" -> {
                        imageFlag.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ukraine))
                    }
                    "Italy" -> {
                        imageFlag.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.italy))
                    }
                    else -> {
                        imageFlag.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(TAG, "Nothing was selected")
            }
        }

        openSuccessButton.setOnClickListener {
            (activity as MainActivity).openFragment(SuccessFragment(), tag = "SuccessFragment")
        }
        openUrlButton.setOnClickListener {
            (activity as MainActivity).openFragment(BrowserFragment(), tag = "BrowserFragment")
        }
        countriesOpenFilePicker.setOnClickListener {
            (activity as MainActivity).openFragment(FilePickerFragment(), tag = "FilePickerFragment")
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.countriesLiveData.observe(viewLifecycleOwner, { _ ->
//            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, countries)
//            albumsListView.adapter = adapter
        })
        viewModel.albumsLiveData.observe(viewLifecycleOwner, { albums ->
            adapter = AlbumRecyclerAdapter(albums) { album -> Log.d(TAG, album.toString()) }
            albumsRecyclerView.adapter = adapter
        })
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}