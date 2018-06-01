package it.riccardomontagnin.locationtracker.pages.journey_details.presenter

import it.riccardomontagnin.locationtracker.model.LocationData
import net.grandcentrix.thirtyinch.TiView

/**
 * Interface representing a generic view inside which all the details of a given journey are
 * displayed to the user.
 */
interface JourneyDetailsView: TiView {

    /**
     * Set the start time of the journey.
     * @param starTime: Starting time of the journey represented as a String.
     */
    fun setStartTime(starTime: String)

    /**
     * Set the end time of the journey.
     * @param endTime: Ending time of the journey represented as a String.
     */
    fun setEndTime(endTime: String)

    /**
     * Set the total journey distance.
     * @param distance: Total distance of the journey, in meters.
     */
    fun setDistance(distance: Double)

    /**
     * Set the duration of the journey.
     * @param duration: Duration of the journey in milliseconds.
     */
    fun setDuration(duration: Long)

    /**
     * Set the average speed that the user has gone during the journey.
     * @param averageSpeed: Average speed during the journey in km/h.
     */
    fun setAverageSpeed(averageSpeed: Double)

    /**
     * Draw all the locations where the user has been during the journey.
     * @param locations: List of locations registered during the journey.
     */
    fun drawLocations(locations: List<LocationData>)

}