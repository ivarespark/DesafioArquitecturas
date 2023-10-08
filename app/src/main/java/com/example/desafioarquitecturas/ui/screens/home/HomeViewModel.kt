package com.example.desafioarquitecturas.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafioarquitecturas.data.Movie
import com.example.desafioarquitecturas.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MoviesRepository): ViewModel() {
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
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.requestMovies()

            repository.movies.collect{
                _state.value = UiState(movies = it)
            }
        }
    }

    fun onMovieClick(movie: Movie){
        viewModelScope.launch{
            // copy: has una copia de movie pero cambiale el valor de favorite
            repository.updateMovie(movie.copy(favorite = !movie.favorite))
        }
    }

    data class UiState(
        val loading : Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}