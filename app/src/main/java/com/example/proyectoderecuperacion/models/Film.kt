package com.example.proyectoderecuperacion.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Film(
    var vote_average: Double = 0.0,
    var id: Int = 0,
    var overview: String? = "",
    var poster_path: String? = "",
    var release_date: String? = "",
    var title: String? = "",
    var favorita: Boolean? = false
) : Serializable, Parcelable {

}