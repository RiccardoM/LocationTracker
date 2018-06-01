package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.model.JourneyData
import java.text.DateFormat

/**
 * Class representing the adapter that will be used inside the view containing the list of all the
 * journeys in order to display each row of the list.
 */
class JourneysAdapter : RecyclerView.Adapter<JourneyViewHolder>() {

    private val journeys: MutableList<JourneyData> = mutableListOf()
    private var onJourneyClicked: (JourneyData) -> Unit = {}

    /**
     * Sets the list of all the journeys that will be displayed inside the view.
     * @param journeys: List of all the journeys to display.
     */
    fun setJourneys(journeys: List<JourneyData>) {
        // Remove every journey that might be exist
        this.journeys.clear()

        // Add all the journeys sorting them by date from the most recent to the least recent
        this.journeys.addAll(journeys.sortedByDescending { it.startingDate })

        // Notify the adapter that the data has changed in order for it to re-draw everything
        notifyDataSetChanged()

    }

    /**
     * Sets the action that must be performed when the user clicks on a row.
     * @param onJourneyClicked: Action to be performed upon the click on a row representing a journey
     * inside the list.
     */
    fun setOnJourneyClicked(onJourneyClicked: (JourneyData) -> Unit) {
        this.onJourneyClicked = onJourneyClicked
    }

    /**
     * @inheritDoc
     */
    override fun getItemCount(): Int = journeys.size

    /**
     * @inheritDoc
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_journey, parent, false)
        return JourneyViewHolder(view)
    }

    /**
     * @inheritDoc
     */
    override fun onBindViewHolder(holder: JourneyViewHolder, position: Int) {
        // Local variable for easier access below
        val journey = journeys[position]

        // Set the details of the journey inside the row view
        holder.setName("Journey ${journeys.size - position}")
        holder.setDate(DateFormat.getDateTimeInstance().format(journey.startingDate))
        holder.itemView.setOnClickListener { onJourneyClicked(journey) }
    }


}