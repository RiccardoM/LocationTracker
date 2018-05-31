package it.riccardomontagnin.locationtracker.pages.main.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val locationRepository: LocationRepository
): TiPresenter<MainView>() {

    private val disposeHandler = RxTiPresenterDisposableHandler(this)
    private var isLocationActivated = false
    private var isJourneyInProgress = false

    override fun onAttachView(view: MainView) {
        super.onAttachView(view)
        view.setJourneyInProgressVisible(isJourneyInProgress)
    }

    override fun onDestroy() {
        // Stop the location tracking when the user kills the application
        locationRepository.stopLocationTracking().blockingAwait()
        super.onDestroy()
    }

    fun setLocationActivated(permissionAccepted: Boolean) {
        this.isLocationActivated = permissionAccepted
    }

    fun trackButtonClicked() {
        // Check for the location first
        when (isLocationActivated) {
            true -> changeJourneyStatus()
            else -> view?.requestPermissions()
        }
    }

    private fun changeJourneyStatus() {
        // Perform the correct action
        when (isJourneyInProgress) {
            true -> stopJourney()
            false -> startJourney()
        }

        // Invert the status of the journey
        isJourneyInProgress = !isJourneyInProgress
    }

    private fun startJourney() {
        disposeHandler.manageDisposable(locationRepository.startLocationTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.setJourneyInProgressVisible(true)
                    view?.showJourneyView()
                }, { Timber.wtf(it) }))
    }

    private fun stopJourney() {
        disposeHandler.manageDisposable(locationRepository.stopLocationTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.setJourneyInProgressVisible(false)
                }))
    }



}