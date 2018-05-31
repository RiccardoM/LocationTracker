package it.riccardomontagnin.locationtracker.pages.journey_details.presenter

import android.location.Location
import it.riccardomontagnin.locationtracker.model.JourneyData
import net.grandcentrix.thirtyinch.TiPresenter
import javax.inject.Inject

class JourneyDetailsPresenter @Inject constructor(
        private val journey: JourneyData
): TiPresenter<JourneyDetailsView>() {

    override fun onAttachView(view: JourneyDetailsView) {
        super.onAttachView(view)
        if (journey.locations.isNotEmpty()) setupJourneyView()
    }

    private fun setupJourneyView() {
        val startLocation = journey.locations.first()
        view?.setStartTime(startLocation.date.toString())

        val endLocation = journey.locations.last()
        view?.setEndTime(endLocation.date.toString())

        val locations = journey.locations
                .map { locationData ->
                    val location = Location("")
                    location.latitude = locationData.latitude
                    location.longitude = locationData.longitude
                    location
                }

        var distance = 0f
        for (i in 1 until locations.size) {
            val firstLocation = locations[i - 1]
            val secondLocation = locations[i]
            distance += firstLocation.distanceTo(secondLocation)
        }
        view?.setDistance(distance.toDouble())

        val time = endLocation.date.time - startLocation.date.time
        view?.setDuration(time)

        val speed = when {
            time == 0L -> 0.0
            distance == 0F -> 0.0
            else -> (distance / 1000) / (time.toDouble() / (1000 * 60 * 60))
        }
        view?.setAverageSpeed(speed)


    }

    fun mapReady() {
        view?.drawLocations(journey.locations)
    }

}