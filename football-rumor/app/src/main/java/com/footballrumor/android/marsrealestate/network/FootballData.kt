package com.footballrumor.android.marsrealestate.network

import com.google.gson.annotations.SerializedName

data class FootballData (
        @SerializedName("id") var id:Int,
        @SerializedName("body") var body : String,
        @SerializedName("image") var image : String,
        @SerializedName("publishedAt") var publishedAt : String,
        @SerializedName("source") var source : String
)