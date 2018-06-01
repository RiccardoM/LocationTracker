package it.riccardomontagnin.locationtracker.injector

import android.content.Context
import dagger.Module
import dagger.Provides
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationTrackerDatabase

/**
 * Dagger [Module] used to provide the database instance used inside the application.
 */
@Module
object DatabaseModule {

    /**
     * Provides the [LocationTrackerDatabase] singleton instance.
     */
    @JvmStatic @Provides fun database(context: Context) = LocationTrackerDatabase.getInstance(context)
}