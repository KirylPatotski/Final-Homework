package com.omisoft.myapplication.mvvm.ui.countries

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.omisoft.myapplication.R

class CountriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        val countriesListView = findViewById<ListView>(R.id.list_countries)
        val countriesArray = resources.getStringArray(R.array.countries)

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, countriesArray)
        countriesListView.adapter = adapter
    }
}