package hse.ru.se_mobile_weather_agregator

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hse.ru.se_mobile_weather_agregator.databinding.ActivityMainBinding
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    private val API_KEY = "dc142bca70ce4a92bca105853221910"
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemModel: WeatherModel
    private var requestQueue: RequestQueue? = null
    private val cityRegex = "^[A-Z]([a-z]|-)*".toRegex()

    data class WeatherModel(
        val temperature: String,
        val pressure: String,
        val humidity: String,
        val condition: String
    )


    private fun getUrl(city: String): String {
        try {
            assert(city.matches(cityRegex))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "1" +
                "&aqi=no&alerts=no"
    }

    @SuppressLint("LongLogTag")
    private val jsonArrayRequest = StringRequest(
        com.android.volley.Request.Method.GET,
        getUrl("Saint-Petersburg"),
        { response ->
            Log.d("Volley on response -- success", response)
            parseWeatherData(response)
        },
        { error ->
            error.message?.let { Log.e("VOLLEY error request", it) }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

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
        itemModel = WeatherModel(
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current").getString("pressure_in"),
            mainObject.getJSONObject("current").getString("humidity"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text")
        )
        Log.d("parseWeatherData", "completed")
    }

    override fun onStart() {
        super.onStart()
        requestQueue = Volley.newRequestQueue(this)
        requestQueue!!.add(jsonArrayRequest)
        Log.i("onStart", "initialize VOLLEY queue")
    }

    fun sendRequest() {
        requestQueue!!.add(jsonArrayRequest)
        Log.i("ButtonClicked", "send VOLLEY request for weather")

    }
}