package com.rahulrv.mvianimalcompose.api

import com.rahulrv.mvianimalcompose.model.Animal
import retrofit2.http.GET

/**
 * Created by  rahulramanujam On 1/12/25
 *
 */
interface AnimalApi {

    @GET("animals.json")
    suspend fun getAnimals():List<Animal>
}