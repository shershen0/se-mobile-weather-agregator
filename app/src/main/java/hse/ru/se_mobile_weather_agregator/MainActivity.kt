package hse.ru.weather

import android.icu.number.Notation.simple
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Address
import android.location.Location
import android.location.LocationManager
import hse.ru.weather.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var temperArray: ArrayList<Double>
    lateinit var typeWeather: ArrayList<String> // "Rain"
    lateinit var humidArray: ArrayList<Double>
    lateinit var pressureArray: ArrayList<Double>
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run("http://api.weatherapi.com/v1/current.json?key=dc142bca70ce4a92bca105853221910&q=Saint-Petersburg&aqi=no")
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                response.body()
            }
        })
    }
}