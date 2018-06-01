package it.riccardomontagnin.locationtracker.pages.journey_list.presenter

import it.riccardomontagnin.locationtracker.model.JourneyData
import net.grandcentrix.thirtyinch.TiView

/**
 * Interface representing a generic view that contains the list of all the journeys that the user
 * has traveled in the past.
 */
interface JourneyListView: TiView {

    /**
     * Sets the journeys to be displayed inside the list.
     * @param journeys: List of objects representing the journeys that should be visible inside the
     * list.
     */
    fun setJourneys(journeys: List<JourneyData>)

    /**
     * Switches the view and shows the details of the given journey.
     * @param journey: Object containing the data of the journey which details should be made
     * visible.
     */
    fun showJourneyDetails(journey: JourneyData)

}