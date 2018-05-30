package it.riccardomontagnin.locationtracker.repository.journeys

import io.reactivex.Completable
import io.reactivex.Single
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.repository.journeys.database.JourneyDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.RoomJourney
import it.riccardomontagnin.locationtracker.repository.journeys.database.RoomLocation
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import java.util.*
import javax.inject.Inject

class JourneyRepositoryImpl @Inject constructor(
        private val journeyDao: JourneyDao,
        private val locationDao: LocationDao
): JourneyRepository {

    override fun saveJourney(journey: JourneyData): Completable {
        return Completable.create { emitter ->
            // Create a unique journey id based on the time in milliseconds
            val journeyId = System.currentTimeMillis().toString()

            // Create a journey to insert inside the database
            val roomJourney = RoomJourney(id = journeyId, date = Date())
            journeyDao.insert(roomJourney)

            // Insert each location of the journey
            journey.locations.asSequence()
                    .map {
                        RoomLocation(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            date = it.date,
                            journeyId = journeyId
                        )
                    }
                    .forEach { locationDao.insert(it) }

            // Complete successfully
            emitter.onComplete()
        }
    }

    override fun getJourneys(): Single<List<JourneyData>> {
        return Single.create { emitter ->
            // Get all the journeys from the database
            val roomJourneys = journeyDao.getAll()


            val journeys = roomJourneys.asSequence()
                    // Get all the locations of the journey, and map the journey and its locations
                    // to a single Pair
                    .map { Pair(it, locationDao.findLocationsForJourneyWithId(it.id)) }

                    // Create the JourneyData object
                    .map { (journey, locations) ->

                        // Convert the locations
                        val journeyLocations = locations.asSequence()
                                .map { LocationData(it.longitude, it.latitude, it.date) }
                                .toList()

                        // Return the JourneyData object
                        JourneyData(locations = journeyLocations, date = journey.date)
                    }


            emitter.onSuccess(journeys.toList())

        }
    }

}