package com.omisoft.myapplication.mvvm.ui.draft.albums.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.ui.draft.albums.ListViewModel

class CountriesActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private lateinit var countriesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_countries)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        lifecycle.addObserver(viewModel)

        countriesListView = findViewById(R.id.list_albums)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.countriesLiveData.observe(this, { countries ->
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, countries)
            countriesListView.adapter = adapter
        })
    }
}