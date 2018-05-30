package it.riccardomontagnin.locationtracker.repository.journeys.database

import android.arch.persistence.room.*
import java.util.*

@Entity(tableName = "locations",
        indices = [(Index("journey_id"))],
        foreignKeys = [(ForeignKey(
                entity = RoomJourney::class,
                parentColumns = ["id"],
                childColumns = ["journey_id"],
                onDelete = ForeignKey.CASCADE
        ))]
)
data class RoomLocation (
        @PrimaryKey(autoGenerate = true) val id: Long? = null,
        val latitude: Double,
        val longitude: Double,
        val date: Date,
        @ColumnInfo(name = "journey_id") val journeyId: String
)