package it.riccardomontagnin.locationtracker.repository.location

import android.content.Context
import android.content.Intent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.model.LocationUpdateEvent
import it.riccardomontagnin.locationtracker.service.LocationService
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val context: Context): LocationRepository {

    /**
     * Object that will be used in order to emit all the updates of the user location.
     * We use a [BehaviorSubject] because we want to make sure that we retrieve the most recent
     * location when we subscribe and then we also get all the subsequent emissions.
     */
    private val locationObservable = BehaviorSubject.create<LocationData>()

    /**
     * Object that will be used in order to emit all the updates of the user location since the last
     * journey has started.
     * We use a [ReplaySubject] because we want to make sure that we retrieve all the location updates
     * since the tracking has started.
     */
    private var journeyLocationObservable = ReplaySubject.create<LocationData>()

    init {
        // Register to the application's EventBus instance in order to get the events that go
        // through it
        EventBus.getDefault().register(this)
    }

    /**
     * Receives all the [LocationUpdateEvent] events that are emitted on the [EventBus] application
     * instance.
     * Once that it catches the events, it forwards the proper values to the [locationObservable],
     * letting all its subscribers know that a new location is available.
     */
    @Subscribe(sticky = true)
    internal fun onLocationUpdatesReceived(locationUpdateEvent: LocationUpdateEvent) {
        EventBus.getDefault().removeStickyEvent(locationUpdateEvent)
        locationObservable.onNext(locationUpdateEvent.newLocation)
        journeyLocationObservable.onNext(locationUpdateEvent.newLocation)
    }

    /**
     * @inheritDoc
     */
    override fun startLocationTracking(): Completable {
        return Completable.create { emitter ->
            // Create a new journey location observable
            journeyLocationObservable = ReplaySubject.create()

            // Start the service to get the location updates
            context.startService(Intent(context, LocationService::class.java))

            // End with success
            emitter.onComplete()
        }
    }

    /**
     * @inheritDoc
     */
    override fun stopLocationTracking(): Completable {
        return Completable.create { emitter ->
            // Complete the journey observable as there won't be any new location since a new
            // journey begins again
            journeyLocationObservable.onComplete()

            // Stop the service to get the location updates
            context.stopService(Intent(context, LocationService::class.java))

            // End with success
            emitter.onComplete()
        }
    }

    /**
     * @inheritDoc
     */
    override fun getLocationUpdates(): Observable<LocationData> = locationObservable

    /**
     * @inheritDoc
     */
    override fun getJourneyLocationUpdates(): Observable<LocationData> = journeyLocationObservable
}