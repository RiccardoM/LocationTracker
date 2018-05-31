package it.riccardomontagnin.locationtracker.pages.journey_list.presenter

import it.riccardomontagnin.locationtracker.model.JourneyData
import net.grandcentrix.thirtyinch.TiView

interface JourneyListView: TiView {
    fun setJourneys(journeys: List<JourneyData>)
    fun showJourneyDetails(journey: JourneyData)
}