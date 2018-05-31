package it.riccardomontagnin.locationtracker.injector

import dagger.Module
import dagger.Provides
import it.riccardomontagnin.locationtracker.repository.journeys.database.JourneyDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationDao
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationTrackerDatabase

/**
 *
 */
@Module(includes = [DatabaseModule::class])
object DaoModule {

    @JvmStatic @Provides fun locationDao(database: LocationTrackerDatabase): LocationDao
            = database.locationDao()

    @JvmStatic @Provides fun journeyDao(database: LocationTrackerDatabase): JourneyDao
            = database.journeyDao()

}