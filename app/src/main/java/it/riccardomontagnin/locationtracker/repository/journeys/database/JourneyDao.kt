package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface JourneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(journey: RoomJourney)

    @Query("SELECT * from journeys INNER JOIN locations ON locations.journey_id = journeys.id")
    fun getAll(): List<RoomJourney>

    @Query("DELETE from journeys")
    fun deleteAll()
}