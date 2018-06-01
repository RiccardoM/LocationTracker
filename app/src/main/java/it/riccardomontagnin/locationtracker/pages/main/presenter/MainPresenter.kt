package it.riccardomontagnin.locationtracker.pages.main.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * Class representing the presenter of the [MainView].
 */
class MainPresenter @Inject constructor(
        private val locationRepository: LocationRepository
): TiPresenter<MainView>() {

    private val disposeHandler = RxTiPresenterDisposableHandler(this)
    private var isLocationActivated = false
    private var isJourneyInProgress = false

    /**
     * @inheritDoc
     */
    override fun onAttachView(view: MainView) {
        super.onAttachView(view)
        view.setJourneyInProgress(isJourneyInProgress)
    }

    /**
     * @inheritDoc
     */
    override fun onDestroy() {
        // Stop the location tracking when the user kills the application
        locationRepository.stopLocationTracking().blockingAwait()
        super.onDestroy()
    }

    /**
     * Handles the changes in the status of the permissions.
     * @param permissionAccepted: `true` if the user has accepted to be tracked, `false` otherwise.
     */
    fun setLocationActivated(permissionAccepted: Boolean) {
        this.isLocationActivated = permissionAccepted
    }

    /**
     * Handles the click to the track button.
     */
    fun trackButtonClicked() {
        // Check for the location first
        when (isLocationActivated) {
            true -> changeJourneyStatus()
            else -> view?.requestPermissions()
        }
    }

    /**
     * Changes the status of the current journey.
     */
    private fun changeJourneyStatus() {
        // Perform the correct action
        when (isJourneyInProgress) {
            true -> stopJourney()
            false -> startJourney()
        }

        // Invert the status of the journey
        isJourneyInProgress = !isJourneyInProgress
    }

    /**
     * Stats a new journey.
     */
    private fun startJourney() {
        disposeHandler.manageDisposable(locationRepository
                // Start the location tracking
                .startLocationTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Notify the view telling it that a new journey has started and bring him to
                // the journey view
                .subscribe({
                    view?.setJourneyInProgress(true)
                    view?.showJourneyView()
                }, { Timber.wtf(it) }))
    }

    /**
     * Stops the journey.
     */
    private fun stopJourney() {
        disposeHandler.manageDisposable(locationRepository
                // Stop the location tracking
                .stopLocationTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Notify the view that the journey is no longer in progress
                .subscribe({ view?.setJourneyInProgress(false) }))
    }



}