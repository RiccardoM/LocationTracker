package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.commonsware.cwac.saferoom.SafeHelperFactory

@Database(entities = [RoomJourney::class, RoomLocation::class], version =1)
@TypeConverters(DateTypeConverter::class)
abstract class LocationTrackerDatabase: RoomDatabase() {
    abstract fun journeyDao(): JourneyDao
    abstract fun locationDao(): LocationDao

    companion object {
        private var INSTANCE: LocationTrackerDatabase? = null

        fun getInstance(context: Context): LocationTrackerDatabase {
            if (INSTANCE == null) {
                synchronized(LocationTrackerDatabase::class) {
                    // Set the database passphrase
                    val factory = SafeHelperFactory("SuperSecretPassword".toCharArray())

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            LocationTrackerDatabase::class.java, "locationtracker.db")
                            // Encrypt it
                            .openHelperFactory(factory)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }

    }

}