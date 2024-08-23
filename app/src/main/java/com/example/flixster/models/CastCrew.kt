package com.example.flixster

import com.google.gson.annotations.SerializedName

class CreditsResponse {
    @SerializedName("cast")
    var cast: List<CastMember>? = null

    @SerializedName("crew")
    var crew: List<CrewMember>? = null
}

class CastMember {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("character")
    var character: String? = null
}

class CrewMember {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("job")
    var job: String? = null
}
