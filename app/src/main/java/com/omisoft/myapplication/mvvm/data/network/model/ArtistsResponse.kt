package com.omisoft.myapplication.mvvm.data.network.model

import com.google.gson.annotations.SerializedName


data class ArtistsResponse(
    @SerializedName("artists") val artists: Artists
)