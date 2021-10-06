package com.example.jsonapp

import com.google.gson.annotations.SerializedName

class CurrencyConverter {

    @SerializedName("date")
    var date: String? = null

    @SerializedName("eur")
    var eur: Datum? = null

    class Datum{
        @SerializedName("inr")
        var inr: Float? = null

        @SerializedName("usd")
        var usd: Float? = null

        @SerializedName("aud")
        var aud: Float? = null

        @SerializedName("sar")
        var sar: Float? = null

        @SerializedName("cny")
        var cny: Float? = null

        @SerializedName("jpy")
        var jpy: Float? = null

    }

}