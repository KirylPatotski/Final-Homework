package com.omisoft.myapplication.mvvm.ui.draft.countries

class CountriesModelImpl : CountriesModel {
    override fun getCountries(): List<String> = listOf("Belarus", "Ukraine", "Poland")
}