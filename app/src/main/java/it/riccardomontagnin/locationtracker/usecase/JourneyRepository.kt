package it.riccardomontagnin.locationtracker.usecase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.LocationData

interface JourneyRepository {
    fun startJourney(): Completable
    fun stopJourney(): Completable
    fun getCurrentJourneyLocations(): Observable<LocationData>
    fun getJourneys(getInProgress: Boolean = false): Single<List<JourneyData>>
}