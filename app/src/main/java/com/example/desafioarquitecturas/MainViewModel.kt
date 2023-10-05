package com.example.desafioarquitecturas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel: ViewModel() {
    /*
    var state by mutableStateOf(UiState())
        private set
    */

    // Con LiveData
    //private val _state = MutableLiveData(UiState())
    // se hace de esta forma para que el state original solo pueda modificarse desde aquí
    // es como un getter publico que solo permite OBSERVAR
    //val state : LiveData<UiState> = _state

    // Con StateFlow
    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state

    init {
        // Lanzo proceso de corutinas para llamar a getMovies()
        viewModelScope.launch {
            //state = UiState(Retrofit.Builder()
            // Con LiveData
            _state.value = UiState(loading = true)
            _state.value = UiState(
                loading = false,
                movies = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesService::class.java)
                .getMovies()
                .results)
        }
    }

    fun onMovieClick(movie: ServerMovie){
        val movies = _state.value.movies.toMutableList()
        // si el id coincide con la pelicula, entonces le cambia el favorito
        movies.replaceAll{ if (it.id == movie.id) movie.copy(favorite = !movie.favorite) else it}
        _state.value = _state.value.copy(movies = movies)
    }

    data class UiState(
        val loading : Boolean = false,
        val movies: List<ServerMovie> = emptyList()
    )
}