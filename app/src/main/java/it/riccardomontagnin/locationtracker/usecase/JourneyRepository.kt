package it.riccardomontagnin.locationtracker.usecase

import io.reactivex.Completable
import io.reactivex.Single
import it.riccardomontagnin.locationtracker.model.JourneyData

interface JourneyRepository {
    fun saveJourney(journey: JourneyData): Completable
    fun getJourneys(): Single<List<JourneyData>>
}