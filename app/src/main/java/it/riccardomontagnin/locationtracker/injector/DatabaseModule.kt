package it.riccardomontagnin.locationtracker.injector

import android.content.Context
import dagger.Module
import dagger.Provides
import it.riccardomontagnin.locationtracker.repository.journeys.database.LocationTrackerDatabase

@Module
object DatabaseModule {
    @JvmStatic @Provides fun database(context: Context) = LocationTrackerDatabase.getInstance(context)
}