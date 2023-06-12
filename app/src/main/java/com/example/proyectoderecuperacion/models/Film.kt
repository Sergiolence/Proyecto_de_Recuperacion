package com.example.proyectoderecuperacion.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Film(
    var genre: List<Genre>? = null,
    var id: Int = 0,
    var overview: String? = "",
    var poster_path: String? = "",
    var release_date: String? = "",
    var title: String? = ""
) : Serializable, Parcelable {

    @Parcelize
    data class Genre(
        var name: String? = ""
    ) : Parcelable
}
