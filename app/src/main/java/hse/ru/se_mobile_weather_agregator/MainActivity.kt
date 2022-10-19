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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    lateinit var temperArray: ArrayList<Double>
    lateinit var typeWeather: ArrayList<String> // "Rain"
    lateinit var humidArray: ArrayList<Double>
    lateinit var pressureArray: ArrayList<Double>
    private val client = OkHttpClient()


//    var client = OkHttpClient()
//    var request = OkHttpRequest(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run("http://api.weatherapi.com/v1/forecast.json?key=dc142bca70ce4a92bca105853221910&q=Saint-Petersburg&days=1&aqi=no&alerts=yes")

        for (tw in typeWeather) {
            if (tw.contains("rain") or tw.contains("rainy")) {
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
                break
            }
        }

        val weather: TextView = binding.weatherType
        weather.text = typeWeather[0]
        val pressure: TextView = binding.pressureValue
        pressure.text = humidArray[0].toString()
        val temperature: TextView = binding.temperatureValue
        temperature.text = temperArray[0].toString()

    }

//
//    class SimpleEntity  (  // no-arg constructor, getters, and setters
//        protected var name: String
//    )


    fun run(url: String) {
        System.out.println("RUNRUNRUNRUNRURNRUN")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("ERROR IN HTTP")
                return
            }
            override fun onResponse(call: Call, response: Response) {
                println("OK")
                println(response.headers())

                val responseData = response.body()?.string()
                runOnUiThread{
                    try {
                        var json = responseData?.let { JSONObject(it) }
                        println("Request Successful!!")
                        println(json)
//                        this@MainActivity.fetchComplete()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }
        })
    }


    fun deserializeWeatherForecast(response: ResponseBody?) {
        val gson = Gson()
        val responseBody = response
        try {

//            mArray = new JSONArray(responseString);
//            for (int i = 0; i < mArray.length(); i++) {
//                JSONObject mJsonObject = mArray.getJSONObject(i);
//                Log.d("OutPut", mJsonObject.getString("NeededString"));
//            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}