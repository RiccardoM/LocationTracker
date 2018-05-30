package it.riccardomontagnin.locationtracker.injector

import dagger.Binds
import dagger.Module
import it.riccardomontagnin.locationtracker.repository.journeys.JourneyRepositoryImpl
import it.riccardomontagnin.locationtracker.repository.location.LocationRepositoryImpl
import it.riccardomontagnin.locationtracker.repository.settings.SettingsRepositoryImpl
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import it.riccardomontagnin.locationtracker.usecase.SettingsRepository

@Module(includes = [DaoModule::class])
abstract class RepositoryModule {
    @Binds abstract fun locationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
    @Binds abstract fun settingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
    @Binds abstract fun journeyRepository(journeyRepository: JourneyRepositoryImpl): JourneyRepository
}