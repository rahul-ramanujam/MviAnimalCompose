package com.rahulrv.mvianimalcompose.view

import com.rahulrv.mvianimalcompose.model.Animal

/**
 * Created by  rahulramanujam On 1/12/25
 *
 */
sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Animals(val animals: List<Animal>) : MainState()
    data class Error(val error: String?) : MainState()

}