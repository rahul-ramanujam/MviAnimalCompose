package com.rahulrv.mvianimalcompose.api

/**
 * Created by  rahulramanujam On 1/12/25
 *
 */
class AnimalRepository(private val api: AnimalApi) {

    suspend fun getAnimals() = api.getAnimals()
}