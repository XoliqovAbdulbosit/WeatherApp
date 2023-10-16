package com.example.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R

class HourAdapter(var array: MutableList<Hour>):RecyclerView.Adapter<HourAdapter.MyHolder>() {
    class MyHolder(var view: View):RecyclerView.ViewHolder(view) {
        var img = view.findViewById<ImageView>(R.id.hour_img)
        var date = view.findViewById<TextView>(R.id.hour_date)
        var temp = view.findViewById<TextView>(R.id.hour_avg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var position = array.get(position)

        holder.date.text = position.hour
        holder.temp.text = position.text
        holder.img.load("https:" + position.img)
    }

}