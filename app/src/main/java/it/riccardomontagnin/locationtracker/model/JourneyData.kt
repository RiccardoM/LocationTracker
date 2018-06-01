package it.riccardomontagnin.locationtracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * [Parcelable] data class representing the data that each journey has.
 */
@Parcelize
data class JourneyData (
        /**
         * List of locations that have been recorded during the journey.
         */
        val locations: List<LocationData>,

        /**
         * Date at which the journey has started.
         */
        val startingDate: Date = Date()
): Parcelable