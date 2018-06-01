package it.riccardomontagnin.locationtracker.model.event

import it.riccardomontagnin.locationtracker.model.LocationData

/**
 * Data class representing the event that is emitted everytime a new location is recorded.
 * @param newLocation: Object representing the data of the newly recorded location.
 */
data class LocationUpdateEvent(val newLocation: LocationData)