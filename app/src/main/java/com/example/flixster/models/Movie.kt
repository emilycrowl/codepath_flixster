package com.example.flixster.models

import com.google.gson.annotations.SerializedName

class Movie {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("overview")
    var description: String? = null

    @SerializedName("poster_path")
    var posterUrl: String? = null

    @SerializedName("backdrop_path")
    var backdropUrl: String? = null

    @SerializedName("tagline")
    var tagline: String? = null

    @SerializedName("genres")
    var genres: List<Genre>? = null
}

class Genre {
    @SerializedName("name")
    var name: String? = null
}
