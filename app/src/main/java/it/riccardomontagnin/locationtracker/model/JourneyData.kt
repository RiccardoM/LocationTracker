package it.riccardomontagnin.locationtracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class JourneyData (
        val locations: List<LocationData>,
        val date: Date = Date()
): Parcelable