package com.omisoft.myapplication.mvvm.data.network.model.weather

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all: Int
)