package com.example.weatherapp

import android.content.ContentValues.TAG
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.Adapters.Day
import com.example.weatherapp.Adapters.DayAdapter
import com.example.weatherapp.Adapters.Hour
import com.example.weatherapp.Adapters.HourAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    val API: String = "b98a9e568d0a4f379b950705230710"
    lateinit var hourlist: MutableList<Hour>
    lateinit var daylist: MutableList<Day>
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.weatherapi.com/v1/forecast.json?key=$API&q=Tashkent&days=8&aqi=no&alerts=no").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                hourlist = mutableListOf()
                daylist = mutableListOf()
                val jsonObj = JSONObject(result)
                val current_temp = jsonObj.getJSONObject("current").getString("temp_c") + "°C"
                val current_img = jsonObj.getJSONObject("current").getJSONObject("condition").getString("icon")
                val current_text = jsonObj.getJSONObject("current").getJSONObject("condition").getString("text")
                val low = jsonObj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("mintemp_c")
                val high = jsonObj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("maxtemp_c")
                val date = jsonObj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getString("date")
                val hourly = jsonObj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour")
                val daily = jsonObj.getJSONObject("forecast").getJSONArray("forecastday")

                for (i in 0..23 step 1) {
                    val hour = hourly.getJSONObject(i).getString("time")
                    var text = hourly.getJSONObject(i).getString("temp_c") + "°C"
                    val img = hourly.getJSONObject(i).getJSONObject("condition").getString("icon")
                    hourlist.add(Hour(img, hour.substring(hour.length - 5, hour.length), text))
                }

                var hour_manager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.hour.adapter = HourAdapter(hourlist)
                binding.hour.layoutManager = hour_manager

                for (i in 0..daily.length() - 1 step 1) {
                    val day = daily.getJSONObject(i).getString("date")
                    val text = daily.getJSONObject(i).getJSONObject("day").getString("avgtemp_c") + "°C"
                    val img = daily.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("icon")
                    daylist.add(Day(img, day, text))
                }

                var day_manager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                binding.day.adapter = DayAdapter(daylist, object: DayAdapter.onItemClick {
                    override fun onDayClick(position: Int) {
                        val hourly = daily.getJSONObject(position).getJSONArray("hour")
                        val date = daily.getJSONObject(position).getString("date")
                        hourlist.clear()
                        for (i in 0..23 step 1) {
                            val hour = hourly.getJSONObject(i).getString("time")
                            var text = hourly.getJSONObject(i).getString("temp_c") + "°C"
                            val img = hourly.getJSONObject(i).getJSONObject("condition").getString("icon")
                            hourlist.add(Hour(img, hour.substring(hour.length - 5, hour.length), text))
                        }
                        findViewById<TextView>(R.id.date).text = date
                        var hour_manager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        binding.hour.adapter = HourAdapter(hourlist)
                        binding.hour.layoutManager = hour_manager
                    }
                })
                binding.day.layoutManager = day_manager

                findViewById<TextView>(R.id.min).text = "L: $low"
                findViewById<TextView>(R.id.max).text = "H: $high"
                findViewById<TextView>(R.id.main).text = current_temp
                findViewById<TextView>(R.id.text).text = current_text
                findViewById<TextView>(R.id.date).text = date
                Glide.with(this@MainActivity).load("https:$current_img").into(findViewById<ImageView>(R.id.img))

            } catch (e: Exception) {

            }
        }
    }
}