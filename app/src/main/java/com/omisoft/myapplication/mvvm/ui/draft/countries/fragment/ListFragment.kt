package com.omisoft.myapplication.mvvm.ui.draft.countries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.ui.draft.browser.BrowserFragment
import com.omisoft.myapplication.mvvm.ui.draft.countries.ListViewModel
import com.omisoft.myapplication.mvvm.ui.draft.filepicker.FilePickerFragment
import com.omisoft.myapplication.success.SuccessFragment

class ListFragment : Fragment() {

    private val viewModel by viewModels<ListViewModel>()
    private lateinit var albumsListView: ListView
    private var adapter: AlbumsBaseAdapter? = null


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
        albumsListView = view.findViewById(R.id.list_albums)

        val openSuccessButton = view.findViewById<AppCompatButton>(R.id.open_success_button)
        val openUrlButton = view.findViewById<AppCompatButton>(R.id.open_url_button)
        val countriesOpenFilePicker = view.findViewById<AppCompatButton>(R.id.open_file_picker_button)

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
        setListeners()
    }

    private fun setListeners() {
        albumsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedAlbum = adapter?.getItem(position)
            println(selectedAlbum)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.countriesLiveData.observe(viewLifecycleOwner, { _ ->
//            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, countries)
//            albumsListView.adapter = adapter
        })
        viewModel.albumsLiveData.observe(viewLifecycleOwner, { albums ->
            adapter = AlbumsBaseAdapter(albums, requireContext())
            albumsListView.adapter = adapter
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