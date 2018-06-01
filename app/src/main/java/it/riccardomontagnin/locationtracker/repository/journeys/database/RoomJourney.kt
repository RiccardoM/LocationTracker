package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "journeys")
data class RoomJourney (
        @PrimaryKey val id: String,
        val date: Date,
        val completed: Boolean = false
)