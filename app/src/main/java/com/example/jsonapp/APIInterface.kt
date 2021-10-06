package com.example.jsonapp

import retrofit2.Call
import retrofit2.http.GET

// to deal with the JSON object in the programming area
interface APIInterface {
    @GET("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/eur.json") //all the API url
    fun doGetListResources(): Call<CurrencyConverter?>?
}