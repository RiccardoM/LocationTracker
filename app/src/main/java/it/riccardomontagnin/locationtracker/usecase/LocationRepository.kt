package it.riccardomontagnin.locationtracker.usecase

import io.reactivex.Completable
import io.reactivex.Observable
import it.riccardomontagnin.locationtracker.model.LocationData

/**
 * Interface representing the way to get the data related to the location, and start or stop the
 * location tracking.
 */
interface LocationRepository {

    /**
     * Starts the location tracking.
     * @return Returns a [Completable] that completes without any error iff the location tracking has
     * been started successfully.
     */
    fun startLocationTracking(): Completable

    /**
     * Stops the location tracking.
     * @return Returns a [Completable] that completes without any error iff the location tracking
     * has been stopped correctly.
     */
    fun stopLocationTracking(): Completable

    /**
     * Gets all the location updates.
     * Note that an item will be emitted immediately representing the last known location, if it is
     * found.
     * @return Returns an [Observable] that never stops emitting the location data. Once subscribed,
     * it will immediately emit an item representing the last known location if it is found.
     */
    fun getLocationUpdates(): Observable<LocationData>


}
