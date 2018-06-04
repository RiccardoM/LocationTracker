package it.riccardomontagnin.locationtracker.pages.journey.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * Class representing the presenter of a generic view that displays the data of the current journey.
 */
class JourneyPresenter @Inject constructor(
        private val locationRepository: LocationRepository,
        private val journeyRepository: JourneyRepository
): TiPresenter<JourneyView>() {

    /**
     * Variable that tells us if the journey tracking is currently in progress or not
     */
    private var trackingEnabled: Boolean = false

    /**
     * Disposable connected to the live update of journey's path
     */
    private var journeyDisposable: Disposable? = null

    /**
     * Variable that will handle all the disposables that we will create based on the life cycle
     * of the view or the life cycle of the presenter
     */
    private val disposeHandler = RxTiPresenterDisposableHandler(this)

    /**
     * Called when the view is attached to the presenter.
     */
    override fun onAttachView(view: JourneyView) {
        super.onAttachView(view)

        when (trackingEnabled) {
            true -> trackJourney()
            false -> getLastLocation()
        }

    }

    /**
     * Enables or disables the tracking location, based on the value given.
     * @param newLocationTrackingStatus: `true` if the location tracking should be enabled, `false`
     * if the tracking should be disabled.
     */
    fun setLocationTrackingEnabled(newLocationTrackingStatus: Boolean) {
        when {
        // If the tracking was in progress, and the user disables it, then stop the journey
            trackingEnabled && !newLocationTrackingStatus -> stopJourney()

            !newLocationTrackingStatus -> getLastLocation()

        // If the user enables the tracking, than start tracking the journey
            else -> trackJourney()
        }

        trackingEnabled = newLocationTrackingStatus
    }

    /**
     * Retrieves the last known location and displays it inside the map.
     */
    private fun getLastLocation() {
        disposeHandler.manageDisposable(locationRepository
                .getLocationUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.showCurrentLocationMapPlaceholder(it)
                }))
    }


    /**
     * Tracks the journey that the user is travelling and displays the updates inside the view.
     */
    private fun trackJourney() {
        if (!trackingEnabled) {
            // Start the journey iff it has not yet been started
            disposeHandler.manageDisposable(journeyRepository
                    // Start the journey
                    .startJourney()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // Show the popup to tell the user
                    .subscribe({ view?.showJourneyStartedPopup() }, { Timber.w(it) }))
        }

        // Stop listening to location events
        journeyDisposable?.dispose()

        // Clear the view wiping out all the locations tracked since now
        view?.clearJourney()

        // Start listening again to the location updates, and get all those that have been emitted
        // since now
        journeyDisposable = journeyRepository
                // Get all the locations for the current journey
                .getCurrentJourneyLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Be sure that the tracking is still enabled before showing the locations
                    if (trackingEnabled) view?.addLocationToCurrentJourney(it)
                }, { Timber.w(it) })

        disposeHandler.manageDisposable(journeyDisposable!!)

    }


    /**
     * Stops the journey and signals the successful stop to the view in order to notify the user.
     */
    private fun stopJourney() {
        disposeHandler.manageDisposable(journeyRepository
                // Stop the journey
                .stopJourney()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Clear the map from the previous journey data and notify the user using a popup
                .subscribe({
                    view?.clearJourney()
                    view?.showJourneyEndedPopup()
                }))
    }

}