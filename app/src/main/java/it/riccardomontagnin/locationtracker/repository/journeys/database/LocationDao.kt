package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: RoomLocation)

    @Query("SELECT * from locations")
    fun getAll(): List<RoomLocation>

    @Query("SELECT * FROM locations WHERE journey_id=:journeyId")
    fun findLocationsForJourneyWithId(journeyId: String): List<RoomLocation>

    @Query("DELETE from locations")
    fun deleteAll()
}