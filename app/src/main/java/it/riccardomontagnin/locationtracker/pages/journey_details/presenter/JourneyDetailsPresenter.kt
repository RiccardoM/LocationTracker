package it.riccardomontagnin.locationtracker.pages.journey_details.presenter

import android.location.Location
import it.riccardomontagnin.locationtracker.model.JourneyData
import net.grandcentrix.thirtyinch.TiPresenter
import javax.inject.Inject

/**
 * Class representing the presenter for a [JourneyDetailsView] intance.
 */
class JourneyDetailsPresenter @Inject constructor(
        private val journey: JourneyData
): TiPresenter<JourneyDetailsView>() {

    /**
     * @inheritDoc
     */
    override fun onAttachView(view: JourneyDetailsView) {
        super.onAttachView(view)

        // Check that there is at least one location inside the journey before setting the view up
        if (journey.locations.isNotEmpty()) setupJourneyView()
    }

    /**
     * Setups all the journey details view.
     */
    private fun setupJourneyView() {
        // Get the start location and display the date as the journey start date
        val startLocation = journey.locations.first()
        view?.setStartTime(startLocation.date.toString())

        // Get the end location and display the date as the journey end date
        val endLocation = journey.locations.last()
        view?.setEndTime(endLocation.date.toString())

        // Get all the locations and map them to Location objects
        val locations = journey.locations
                .map { locationData ->
                    val location = Location("")
                    location.latitude = locationData.latitude
                    location.longitude = locationData.longitude
                    location
                }


        var distance = 0f

        // Compute the total distance that the user traveled during the journey by computing the
        // distance between every couple of locations that have been tracked one after the other.
        // To do so, cycle trough all the locations and compute the distance between the ith location
        // and the one before it. Than sum everything up inside the `distance` accumulator variable.
        for (i in 1 until locations.size) {
            val firstLocation = locations[i - 1]
            val secondLocation = locations[i]
            distance += firstLocation.distanceTo(secondLocation)
        }

        // Display the total distance
        view?.setDistance(distance.toDouble())

        // Compute the time spent traveling by subtracting the start time from the end time and
        // show it inside the view
        val time = endLocation.date.time - startLocation.date.time
        view?.setDuration(time)

        // Compute the speed
        val speed = when {
            time == 0L -> 0.0
            distance == 0F -> 0.0

            // Divide the distance by 1000 in order to get the number of kilometers traveled
            // Then divide the milliseconds spent traveling by the number of milliseconds in an hour
            // Finally, divide the Km distance by the hours time to get a Km/h value
            else -> (distance / 1000) / (time.toDouble() / (1000 * 60 * 60))
        }

        // Display the average speed
        view?.setAverageSpeed(speed)

    }

    /**
     * Called when the view has the map ready in order to work with it.
     */
    fun mapReady() {
        // Display all the journey locations
        view?.drawLocations(journey.locations)
    }

}