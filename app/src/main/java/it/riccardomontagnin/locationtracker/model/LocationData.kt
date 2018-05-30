package it.riccardomontagnin.locationtracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class LocationData (
        val longitude: Double,
        val latitude: Double,
        val date: Date
): Parcelable