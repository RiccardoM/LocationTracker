package it.riccardomontagnin.locationtracker.repository.journeys

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.repository.journeys.database.JourneyDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.RoomJourney
import it.riccardomontagnin.locationtracker.repository.journeys.database.RoomLocation
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import java.util.*
import javax.inject.Inject

/**
 * @see JourneyRepositoryImpl
 */
class JourneyRepositoryImpl @Inject constructor(
        private val journeyDao: JourneyDao,
        private val locationDao: LocationDao,
        private val locationRepository: LocationRepository
): JourneyRepository {

    private var locationsDisposable: Disposable? = null
    private var currentJourneyId = ""
    private var currentJourneyLocationsObservable = ReplaySubject.create<LocationData>()

    /**
     * @inheritDoc
     */
    override fun startJourney(): Completable {
        // Create a unique journey id
        currentJourneyId = System.currentTimeMillis().toString()

        // Create a new ReplaySubject to clean all the previous locations
        currentJourneyLocationsObservable = ReplaySubject.create()

        // Return a Completable that saves the journey inside the database
        return Completable.create { emitter ->
            val roomJourney = RoomJourney(id = currentJourneyId, date = Date())
            journeyDao.insert(roomJourney)

            // Start observing for new location updates
            locationsDisposable = locationRepository.getLocationUpdates()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(this::saveAndEmitLocation)

            emitter.onComplete()
        }
    }

    /**
     * @inheritDoc
     */
    override fun stopJourney(): Completable {
        return Completable.create { emitter ->
            // Set the journey as completed
            val roomJourney = journeyDao.findById(currentJourneyId).copy(completed = true)
            journeyDao.update(roomJourney)

            // Clean the journey id
            currentJourneyId = ""

            // Complete the observable
            currentJourneyLocationsObservable.onComplete()

            // Dispose the observable for the previous journey
            locationsDisposable?.dispose()

            // Complete
            emitter.onComplete()
        }
    }

    /**
     * @inheritDoc
     */
    private fun saveAndEmitLocation(locationData: LocationData) {
        // Convert the location data
        val location = RoomLocation(
                latitude = locationData.latitude,
                longitude = locationData.longitude,
                date = locationData.date,
                journeyId = currentJourneyId
        )

        // Insert the location inside the database
        locationDao.insert(location)

        // Emit the location update
        currentJourneyLocationsObservable.onNext(locationData)
    }

    /**
     * @inheritDoc
     */
    override fun getCurrentJourneyLocations(): Observable<LocationData> {
        return currentJourneyLocationsObservable
    }

    /**
     * @inheritDoc
     */
    override fun getJourneys(getInProgress: Boolean): Single<List<JourneyData>> {
        return Single.create { emitter ->
            // Get all the journeys from the database based on what the user asks for
            val roomJourneys = when (getInProgress) {
                true -> journeyDao.getAll()
                false -> journeyDao.getAll().filter { it.completed }
            }

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
                        JourneyData(locations = journeyLocations, startingDate = journey.date)
                    }
                    .toList()


            emitter.onSuccess(journeys)

        }
    }

}