package com.footballrumor.android.marsrealestate.network

import com.google.gson.annotations.SerializedName

class FootballList(@SerializedName("data") var data :List<FootballData>,
                   @SerializedName("page") var page : pageData
)