package com.omisoft.myapplication.mvvm.ui.countries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.ui.countries.CountriesViewModel
import com.omisoft.myapplication.success.SuccessFragment

class CountriesFragment : Fragment() {

    private lateinit var viewModel: CountriesViewModel
    private lateinit var countriesListView: ListView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CountriesViewModel::class.java]

        lifecycle.addObserver(viewModel)

        countriesListView = view.findViewById(R.id.list_countries)
        val openSuccessButton = view.findViewById<AppCompatButton>(R.id.open_success_button)

        openSuccessButton.setOnClickListener {
            (activity as MainActivity).openFragment(SuccessFragment())
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.countriesLiveData.observe(viewLifecycleOwner, { countries ->
            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, countries)
            countriesListView.adapter = adapter
        })
    }
}