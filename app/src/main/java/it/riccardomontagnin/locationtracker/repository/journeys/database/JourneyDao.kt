package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.*

@Dao
interface JourneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(journey: RoomJourney)

    @Update
    fun update(journey: RoomJourney)

    @Query("SELECT * FROM journeys WHERE id=:id")
    fun findById(id: String): RoomJourney

    @Query("SELECT * from journeys")
    fun getAll(): List<RoomJourney>

    @Query("DELETE from journeys")
    fun deleteAll()
}