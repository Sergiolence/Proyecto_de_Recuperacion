package com.example.proyectoderecuperacion.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchAPI {
    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<SearchResponse>
}
