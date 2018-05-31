package it.riccardomontagnin.locationtracker.pages.journey.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

class JourneyPresenter @Inject constructor(
        private val locationRepository: LocationRepository,
        private val journeyRepository: JourneyRepository
): TiPresenter<JourneyView>() {

    private var trackingEnabled: Boolean = false
    private val disposeHandler = RxTiPresenterDisposableHandler(this)

    override fun onAttachView(view: JourneyView) {
        super.onAttachView(view)

        when (trackingEnabled) {
            true -> trackJourney()
            false -> getLastLocation()
        }

    }

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

    private fun getLastLocation() {
        disposeHandler.manageDisposable(locationRepository
                .getLocationUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.showCurrentLocationMapPlaceholder(it)
                }))
    }

    private fun trackJourney() {
        if (!trackingEnabled) {
            disposeHandler.manageDisposable(journeyRepository
                    // Start the journey
                    .startJourney()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // Show the popup to tell the user
                    .subscribe({ view?.showJourneyStartedPopup() }))
        }


        disposeHandler.manageViewDisposable(journeyRepository
                // Get all the locations for the current journey
                .getCurrentJourneyLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Be sure that the tracking is still enabled before showing the locations
                    if (trackingEnabled) view?.addLocationToCurrentJourney(it)
                }, { Timber.w(it) }))
    }


    private fun stopJourney() {
        disposeHandler.manageDisposable(journeyRepository.stopJourney()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.clearJourney()
                    view?.showJourneyEndedPopup()
                }))
    }

}