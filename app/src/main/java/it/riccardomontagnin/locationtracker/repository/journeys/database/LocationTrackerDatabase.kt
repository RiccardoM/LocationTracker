package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.Environment
import java.io.File

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
                    val dataDirectory = Environment.getDataDirectory()
                    val dbDirectory = File(dataDirectory, "LocationTracker").absolutePath
                    val dbName = dbDirectory + File.separator + "locationtracker.db"


                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            LocationTrackerDatabase::class.java, dbName)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }

    }

}