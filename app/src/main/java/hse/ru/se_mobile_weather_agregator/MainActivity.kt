package hse.ru.se_mobile_weather_agregator

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
}