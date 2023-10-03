package com.example.desafioarquitecturas

import retrofit2.http.GET

interface MoviesService {
    @GET("discover/movie?api_key=b821b4dfd99676abc250c7fa8a664694")
    suspend fun getMovies(): MovieResult // suspended: convertir codigo sincrono a asyncrono
}