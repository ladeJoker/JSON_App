package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    //declare all needed variables
    lateinit var dateView: TextView
    lateinit var userInput: EditText
    lateinit var spinner: Spinner
    lateinit var convertButton: Button
    lateinit var getDataOfJSON: CurrencyConverter
    private var selectedCurr: Float? = null
    lateinit var resultView: TextView
    lateinit var appAlert: ConstraintLayout

    //these values will be fill by the JSON object data!
    private var date: String? = null
    private var inr: Float? = null
    private var usd: Float? = null
    private var aud: Float? = null
    private var sar: Float? = null
    private var cny: Float? = null
    private var jpy: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinner = findViewById(R.id.spinner)
        convertButton = findViewById(R.id.button)
        userInput = findViewById(R.id.etUserValue)
        dateView = findViewById(R.id.tvDate)
        resultView = findViewById(R.id.tvResult)
        appAlert = findViewById(R.id.mainXml)

        //------------------------------------------------------------------------------------------ prepare for API
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java) //required
        val call: Call<CurrencyConverter?>? = apiInterface!!.doGetListResources() //return targeted object class details
        Log.d("MainActivity", "Error1---------------------------------------")
        //------------------------------------------------------------------------------------------ API handler - start
        call?.enqueue(object : Callback<CurrencyConverter?> {
            override fun onResponse(
                call: Call<CurrencyConverter?>?, // set the targeted object
                response: Response<CurrencyConverter?> // set the targeted object
            ) {
                //get the data from JSON object here
                getDataOfJSON = response.body()!!

                try {
                    date = getDataOfJSON.date.toString()
                    inr = getDataOfJSON.eur?.inr
                    usd = getDataOfJSON.eur?.usd
                    aud = getDataOfJSON.eur?.aud
                    sar = getDataOfJSON.eur?.sar
                    cny = getDataOfJSON.eur?.cny
                    jpy = getDataOfJSON.eur?.jpy
                }catch (e: Exception){
                    Log.d("MainActivity", "API Data retrieve ERROR")
                }
            }

            override fun onFailure(call: Call<CurrencyConverter?>, t: Throwable?) { //required to check if there is failure
                call.cancel()
            }

        })

        //------------------------------------------------------------------------------------------ API handler - end
        Log.d("MainActivity", "$selectedCurr Error3---------------------------------------")

        //------------------------------------------------------------------------------------------Handle Spinner
        //retrive the string array from String file
        val Currency = resources.getStringArray(R.array.Currency)//variable to store the picked currency from the spinner
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, Currency
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //write handle process
                    try {
                        when(position){
                            0 -> selectedCurr = inr
                            1 -> selectedCurr = usd
                            2 -> selectedCurr = aud
                            3 -> selectedCurr = sar
                            4 -> selectedCurr = cny
                            5 -> selectedCurr = jpy
                            else -> {
                                Snackbar.make(appAlert, "Select Currency!", Snackbar.LENGTH_LONG).show()
                            }
                        }

                    }catch (e: Exception){
                        Log.d("MainActivity", "Error4---------------------------------------")
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Snackbar.make(appAlert, "Default currency select: inr!!", Snackbar.LENGTH_LONG).show()
                    selectedCurr = inr
                }
            }
        }

        convertButton.setOnClickListener { convertCurrency() }
    }

    fun convertCurrency(){
        var input = userInput.text.toString()
        Log.d("MainActivity", "$input Error5---------------------------------------")

        var inputFloat: Float = 0.0f
        try {
            inputFloat =  input.toFloat()
        }catch (e: Exception){
            Log.d("MainActivity", "Convert to flout error")
        }
        var result: Float = inputFloat!! * selectedCurr!!
        resultView.setText(result.toString())
        dateView.setText("Date: $date")

    }
}