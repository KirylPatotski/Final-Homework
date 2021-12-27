package com.omisoft.myapplication.mvvm.data.network.model.artists

import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("artist") val artist: List<Artist>,
    @SerializedName("@attr") val attr: Attr
)