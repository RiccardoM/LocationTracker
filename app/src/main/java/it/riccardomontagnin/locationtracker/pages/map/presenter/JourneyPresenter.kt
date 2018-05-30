package it.riccardomontagnin.locationtracker.pages.map.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import javax.inject.Inject

class JourneyPresenter @Inject constructor(
        private val locationRepository: LocationRepository
): TiPresenter<JourneyView>() {

    private var trackingEnabled: Boolean = false
    private var locationDisposable: Disposable? = null
    private var journeyDisposable: Disposable? = null

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

            // If the user enables the tracking, than start tracking the journey
            newLocationTrackingStatus -> trackJourney()

            // If the tracking is disabled, and the user does not enable it, then just get the last
            // location
            else -> getLastLocation()
        }

        trackingEnabled = newLocationTrackingStatus
    }

    private fun getLastLocation() {
        locationDisposable = locationRepository.getLocationUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.showCurrentLocationMapPlaceholder(it)
                })
        disposeHandler.manageViewDisposable(locationDisposable!!)
    }

    private fun trackJourney() {
        journeyDisposable = locationRepository.getJourneyLocationUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Be sure that the tracking is still enabled before showing the location
                    if (trackingEnabled) view?.addLocationToCurrentJourney(it)
                })
        disposeHandler.manageViewDisposable(journeyDisposable!!)
    }

    private fun stopJourney() {
        // Stop observing journey updates
        journeyDisposable?.dispose()

        // Clear the view journeys
        view?.clearJourney()

    }

}