package com.rahulrv.mvianimalcompose.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahulrv.mvianimalcompose.api.AnimalApi
import com.rahulrv.mvianimalcompose.api.AnimalRepository

/**
 * Created by  rahulramanujam On 1/12/25
 *
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val api: AnimalApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(AnimalRepository(api= api)) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}