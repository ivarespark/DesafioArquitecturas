package com.example.desafioarquitecturas.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafioarquitecturas.data.Movie
import com.example.desafioarquitecturas.data.local.MoviesDao
import com.example.desafioarquitecturas.data.local.toLocalMovie
import com.example.desafioarquitecturas.data.local.toMovie
import com.example.desafioarquitecturas.data.remote.MoviesService
import com.example.desafioarquitecturas.data.remote.toLocalMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(private val dao: MoviesDao): ViewModel() {
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
            val isDbEmpty = dao.count() == 0
            if (isDbEmpty){
                _state.value = UiState(loading = true)
                dao.insertAll(
                    Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MoviesService::class.java)
                        .getMovies()
                        .results
                        .map { it.toLocalMovie()}
                )
            }

            dao.getMovies().collect{ movies ->
                _state.value = UiState(
                    loading = false,
                    movies = movies.map { it.toMovie() }
                )
            }
        }
    }

    fun onMovieClick(movie: Movie){
        viewModelScope.launch{
            // copy: has una copia de movie pero cambiale el valor de favorite
            dao.updateMovie(movie.copy(favorite = !movie.favorite).toLocalMovie())
        }
    }

    data class UiState(
        val loading : Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}