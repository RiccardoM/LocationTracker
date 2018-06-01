package it.riccardomontagnin.locationtracker.pages.journey.presenter

import it.riccardomontagnin.locationtracker.model.LocationData
import net.grandcentrix.thirtyinch.TiView

/**
 * Interface representing a view inside which the user can see the representation of the current
 * journey.
 */
interface JourneyView: TiView {

    /**
     * Displays a marker inside the map at the given location.
     * @param location: Object representing the location at which the marker should be placed.
     */
    fun showCurrentLocationMapPlaceholder(location: LocationData)

    /**
     * Adds the given location as a location of the current journey, updating also the path of the
     * journey itself.
     * @param location: Object representing the location that should be added as the current journey
     * new location.
     */
    fun addLocationToCurrentJourney(location: LocationData)

    /**
     * Clears the journey removing all the data from the map.
     */
    fun clearJourney()

    /**
     * Shows a popup telling the user that the journey has started.
     */
    fun showJourneyStartedPopup()

    /**
     * Shows a popup telling the user that the journey has ended.
     */
    fun showJourneyEndedPopup()

}