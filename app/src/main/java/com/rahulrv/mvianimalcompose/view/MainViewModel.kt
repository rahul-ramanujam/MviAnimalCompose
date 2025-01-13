package com.rahulrv.mvianimalcompose.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rahulrv.mvianimalcompose.api.AnimalRepository
import com.rahulrv.mvianimalcompose.api.AnimalService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 * Created by  rahulramanujam On 1/12/25
 *
 */
class MainViewModel(private val repo: AnimalRepository) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private var _state = mutableStateOf<MainState>(MainState.Idle)

    val state = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch{
            userIntent.consumeAsFlow().collect { collector ->
                when(collector) {
                    is MainIntent.FetchAnimals -> fetchAnimals()
                }
            }
        }
    }

    private fun fetchAnimals() {
        viewModelScope.launch {
            state.value = MainState.Loading
            state.value = try {
                MainState.Animals(repo.getAnimals())
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(repo = AnimalRepository(AnimalService.api))
            }
        }
    }
}