package it.riccardomontagnin.locationtracker.pages.journey.presenter

import it.riccardomontagnin.locationtracker.model.LocationData
import net.grandcentrix.thirtyinch.TiView

interface JourneyView: TiView {
    fun showCurrentLocationMapPlaceholder(location: LocationData)
    fun addLocationToCurrentJourney(location: LocationData)
    fun clearJourney()
    fun showJourneyStartedPopup()
    fun showJourneyEndedPopup()
}