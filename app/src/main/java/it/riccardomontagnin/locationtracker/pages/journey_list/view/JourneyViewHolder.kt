package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import it.riccardomontagnin.locationtracker.R

class JourneyViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val nameTextView = view.findViewById<TextView>(R.id.viewholder_journey_name)
    private val dateTextView = view.findViewById<TextView>(R.id.viewholder_journey_date)

    fun setName(name: String) {
        nameTextView.text = name
    }

    fun setDate(date: String) {
        dateTextView.text = date
    }

}