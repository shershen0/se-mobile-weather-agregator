package hse.ru.se_mobile_weather_agregator

import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest

//import hse.ru.weather.databinding.ActivityMainBinding
import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hse.ru.se_mobile_weather_agregator.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.android.volley.toolbox.Volley
import okhttp3.*
import com.google.gson.Gson;
import  com.android.volley.Request

class MainActivity : AppCompatActivity() {
    private val API_KEY = "dc142bca70ce4a92bca105853221910"
    lateinit var binding: ActivityMainBinding
    lateinit var itemModel: WeatherModel

    data class WeatherModel(
        val temperature: String,
        val pressure: String,
        val humidity: String,
        val condition: String
    )

    var requestQueue: RequestQueue? = null

    val key = "7ab76c22a12f41ff929105944223011"

    private fun getUrl(city: String): String {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                key +
                "&q=" +
                city +
                "&days=" +
                "1" +
                "&aqi=no&alerts=no"
    }

    private val jsonArrayRequest = StringRequest(
        Request.Method.GET,
        getUrl("Saint-Petersburg"),
        { response ->
            print("Successfully")
            Log.d("Volley on response", response)
        },
        { error ->
            print(error)
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding = ActivityMainBinding.inflate(layoutInflater, R.layout.activity_main)
//        setContentView(binding.root)
//        binding.activity = this

//        var button: Button = findViewById(R.id.button1)
//        button.setOnClickListener{
//            requestQueue!!.add(jsonArrayRequest)
//            Log.i("ButtonClicked", "send VOLLEY request for weather")
//
//        }


        if (itemModel.condition.contains("rain") or itemModel.condition.contains("rainy")) {
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("Weather Agregator")
                .setContentText("Rain is forecasted today")

            val notification: Notification = builder.build()
            val notificationManag = NotificationManagerCompat.from(this)
            notificationManag.notify(1, builder.build())

            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }

        val temperature: TextView = binding.temperatureValue
        temperature.text = itemModel.temperature
        val pressure: TextView = binding.pressureValue
        pressure.text = itemModel.pressure
        val humidity: TextView = binding.humidityValue
        humidity.text = itemModel.humidity
        val weather: TextView = binding.weatherType
        weather.text = itemModel.condition
    }

    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val item = WeatherModel(
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current").getString("pressure_in"),
            mainObject.getJSONObject("current").getString("humidity"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text")
        )


    }

    override fun onStart() {
        super.onStart()
        requestQueue = Volley.newRequestQueue(this)
//        requestQueue!!.add(jsonArrayRequest)
        Log.i("onStart", "initialize VOLLEY queue")
    }

    fun sendRequest() {
        requestQueue!!.add(jsonArrayRequest)

    }


}
