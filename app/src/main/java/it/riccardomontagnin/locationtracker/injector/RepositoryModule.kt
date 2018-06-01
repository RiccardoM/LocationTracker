package it.riccardomontagnin.locationtracker.injector

import dagger.Binds
import dagger.Module
import it.riccardomontagnin.locationtracker.repository.journeys.JourneyRepositoryImpl
import it.riccardomontagnin.locationtracker.repository.location.LocationRepositoryImpl
import it.riccardomontagnin.locationtracker.repository.settings.SettingsRepositoryImpl
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import it.riccardomontagnin.locationtracker.usecase.SettingsRepository

/**
 * Dagger [Module] used in order to provide all the repositories that can be used inside the
 * application.
 */
@Module(includes = [DaoModule::class])
abstract class RepositoryModule {

    /**
     * Binds [LocationRepositoryImpl] to [LocationRepository] so that every time an object requires
     * an instance of [LocationRepository] as a dependency, [locationRepository] will be
     * returned.
     */
    @Binds abstract fun locationRepository(locationRepository: LocationRepositoryImpl): LocationRepository

    /**
     * Binds [SettingsRepositoryImpl] to [SettingsRepository] so that every time an object requires
     * an instance of [SettingsRepository] as a dependency, [settingsRepository] will be
     * returned.
     */
    @Binds abstract fun settingsRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository

    /**
     * Binds [JourneyRepositoryImpl] to [JourneyRepository] so that every time an object requires
     * an instance of [JourneyRepository] as a dependency, [journeyRepository] will be
     * returned.
     */
    @Binds abstract fun journeyRepository(journeyRepository: JourneyRepositoryImpl): JourneyRepository
}