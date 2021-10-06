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
    private var input: String = ""
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
                    callJSON(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    Snackbar.make(appAlert, "Default currency select: inr!!", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        convertButton.setOnClickListener { convertCurrency() }
    }

    fun convertCurrency(){
        var inputFloat: Float = 0.0f
        input = userInput.text.toString()
        try {
            inputFloat = input.toFloat()
        }catch (e: Exception){
            Log.d("MainActivity", "Convert to flout error")
        }
        var result: Float = inputFloat * selectedCurr!!
        resultView.setText(result.toString())
        dateView.setText("Date: $date")
    }

    fun callJSON(index: Int){
        //------------------------------------------------------------------------------------------ prepare for API
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java) //required
        val call: Call<CurrencyConverter?>? = apiInterface!!.doGetListResources() //return targeted object class details

        //------------------------------------------------------------------------------------------ API handler - start
        call?.enqueue(object : Callback<CurrencyConverter?> {
            override fun onResponse(
                call: Call<CurrencyConverter?>?, // set the targeted object
                response: Response<CurrencyConverter?> // set the targeted object
            ) {
                //get the data from JSON object here
                getDataOfJSON = response.body()!!
                date = getDataOfJSON.date.toString()

                when(index){
                    0 -> selectedCurr = getDataOfJSON.eur?.inr
                    1 -> selectedCurr = getDataOfJSON.eur?.usd
                    2 -> selectedCurr = getDataOfJSON.eur?.aud
                    3 -> selectedCurr = getDataOfJSON.eur?.sar
                    4 -> selectedCurr = getDataOfJSON.eur?.cny
                    5 -> selectedCurr = getDataOfJSON.eur?.jpy
                    else -> selectedCurr = getDataOfJSON.eur?.inr
                }
            }
            override fun onFailure(call: Call<CurrencyConverter?>, t: Throwable?) { //required to check if there is failure
                call.cancel()
            }
        })
    }

}