package com.example.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R

class DayAdapter(var array: MutableList<Day>, var listener: onItemClick):RecyclerView.Adapter<DayAdapter.MyHolder>() {
    class MyHolder(var view: View):RecyclerView.ViewHolder(view) {
        var img = view.findViewById<ImageView>(R.id.day_img)
        var date = view.findViewById<TextView>(R.id.day_date)
        var temp = view.findViewById<TextView>(R.id.day_avg)
        var day_main = view.findViewById<LinearLayout>(R.id.day_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyHolder, posit: Int) {
        var position = array.get(posit)

        holder.date.text = position.day
        holder.temp.text = position.text
        holder.img.load("https:" + position.img)

        holder.day_main.setOnClickListener {
            listener.onDayClick(posit)
        }
    }

    interface onItemClick{
        fun onDayClick(position: Int)
    }

}