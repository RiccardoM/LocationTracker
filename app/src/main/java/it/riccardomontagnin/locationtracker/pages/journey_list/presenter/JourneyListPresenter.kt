package it.riccardomontagnin.locationtracker.pages.journey_list.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import timber.log.Timber
import javax.inject.Inject

class JourneyListPresenter @Inject constructor(
        private val journeyRepository: JourneyRepository
): TiPresenter<JourneyListView>() {

    private val handler = RxTiPresenterDisposableHandler(this)

    override fun onAttachView(view: JourneyListView) {
        super.onAttachView(view)
        updateList()
    }

    fun updateList() {
        handler.manageViewDisposable(journeyRepository.getJourneys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.setJourneys(it) }, { Timber.w(it) }))
    }

    fun journeyClicked(journey: JourneyData) {
        view?.showJourneyDetails(journey)
    }

}