package it.riccardomontagnin.locationtracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * [Parcelable] data class representing the data that a location contains.
 */
@Parcelize
data class LocationData (
        /**
         * Longitude of the location.
         */
        val longitude: Double,

        /**
         * Latitude of the location.
         */
        val latitude: Double,

        /**
         * Date at which the location has been recorded.
         */
        val date: Date = Date()
): Parcelable