package it.riccardomontagnin.locationtracker.usecase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.LocationData

/**
 * Interface used in order to retrieve information about journeys.
 */
interface JourneyRepository {

    /**
     * Starts a new journey.
     * @return Returns a [Completable] that completes without error if the journey is started
     * successfully, or thows any error that might come up when the journey fails to start.
     */
    fun startJourney(): Completable

    /**
     * Stops the current journey.
     * @return Returns a [Completable] that completes successfully when the current journey is
     * stopped correctly, or throws any other error instead.
     */
    fun stopJourney(): Completable

    /**
     * Returns all the locations that have been recorded since the start of the current journey.
     * @return Returns an [Observable] that emits all the location recorded since the start of the
     * current journey, one by one.
     */
    fun getCurrentJourneyLocations(): Observable<LocationData>

    /**
     * Returns the list of all the journeys.
     * @param getInProgress: `true` if the current journey should be included inside the list of
     * journeys, `false` if only the concluded journeys should be retrieved.
     */
    fun getJourneys(getInProgress: Boolean = false): Single<List<JourneyData>>

}