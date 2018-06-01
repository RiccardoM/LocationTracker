package it.riccardomontagnin.locationtracker.injector

import dagger.Module
import dagger.Provides
import it.riccardomontagnin.locationtracker.repository.journeys.database.JourneyDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationTrackerDatabase

/**
 * Dagger [Module] used to provide all the DAOs that could be useful inside the application.
 * This module includes the [DatabaseModule] to get the instance of the database from which to get
 * the DAOs.
 */
@Module(includes = [DatabaseModule::class])
object DaoModule {

    /**
     * Provides the [LocationDao] singleton instance.
     */
    @JvmStatic @Provides fun locationDao(database: LocationTrackerDatabase): LocationDao
            = database.locationDao()

    /**
     * Provides the [LocationDao] singleton instance.
     */
    @JvmStatic @Provides fun journeyDao(database: LocationTrackerDatabase): JourneyDao
            = database.journeyDao()

}