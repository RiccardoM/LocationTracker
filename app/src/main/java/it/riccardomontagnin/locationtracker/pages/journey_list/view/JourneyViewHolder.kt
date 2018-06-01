package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import it.riccardomontagnin.locationtracker.R

/**
 * Class representing the holder that will hold the view that represents a single journey inside the
 * list of journeys traveled by the user.
 */
class JourneyViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val nameTextView = view.findViewById<TextView>(R.id.viewholder_journey_name)
    private val dateTextView = view.findViewById<TextView>(R.id.viewholder_journey_date)

    /**
     * Sets the text to be displayed as the name of the journey.
     * @param name: String representing the name of the journey.
     */
    fun setName(name: String) {
        nameTextView.text = name
    }

    /**
     * Sets the data of the journey.
     * @param date: String representing the date of the journey.
     */
    fun setDate(date: String) {
        dateTextView.text = date
    }

}