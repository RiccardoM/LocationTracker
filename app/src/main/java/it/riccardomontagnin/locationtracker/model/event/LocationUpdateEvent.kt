package it.riccardomontagnin.locationtracker.model.event

import it.riccardomontagnin.locationtracker.model.LocationData

data class LocationUpdateEvent(val newLocation: LocationData)