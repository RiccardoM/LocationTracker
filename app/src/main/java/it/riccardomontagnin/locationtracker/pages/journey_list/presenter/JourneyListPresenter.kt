package it.riccardomontagnin.locationtracker.pages.journey_list.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * Class representing the presenter for the [JourneyListView].
 */
class JourneyListPresenter @Inject constructor(
        private val journeyRepository: JourneyRepository
): TiPresenter<JourneyListView>() {

    private val handler = RxTiPresenterDisposableHandler(this)

    override fun onAttachView(view: JourneyListView) {
        super.onAttachView(view)
        updateList()
    }

    /**
     * Updates the list of the journeys that the user has traveled in the past.
     */
    fun updateList() {
        handler.manageViewDisposable(journeyRepository
                // Retrieve the journeys
                .getJourneys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Update the view
                .subscribe({ view?.setJourneys(it) }, { Timber.w(it) }))
    }

    /**
     * Handles the click on a journey inside the journeys list.
     * @param journey: Object containing the information of the journey that is represented by the
     * clicked row.
     */
    fun journeyClicked(journey: JourneyData) {
        // Shows the journey details
        view?.showJourneyDetails(journey)
    }

}