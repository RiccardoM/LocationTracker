package it.riccardomontagnin.locationtracker.pages.journey_list.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.model.JourneyData
import java.text.DateFormat

class JourneysAdapter : RecyclerView.Adapter<JourneyViewHolder>() {

    private val journeys: MutableList<JourneyData> = mutableListOf()
    private var onJourneyClicked: (JourneyData) -> Unit = {}

    fun setJourneys(journeys: List<JourneyData>) {
        this.journeys.clear()
        this.journeys.addAll(journeys.sortedByDescending { it.date })
        notifyDataSetChanged()
    }

    fun setOnJourneyClicked(onJourneyClicked: (JourneyData) -> Unit) {
        this.onJourneyClicked = onJourneyClicked
    }

    override fun getItemCount(): Int = journeys.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_journey, parent, false)
        return JourneyViewHolder(view)
    }

    override fun onBindViewHolder(holder: JourneyViewHolder, position: Int) {
        // Local variable for easier access below
        val journey = journeys[position]

        holder.setName("Journey ${journeys.size - position}")
        holder.setDate(DateFormat.getDateTimeInstance().format(journey.date))
        holder.itemView.setOnClickListener { onJourneyClicked(journey) }

    }


}