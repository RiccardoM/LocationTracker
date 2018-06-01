package it.riccardomontagnin.locationtracker.model.event

/**
 * Event representing that the location tracking should be visualized or not.
 * @param trackingEnabled: `true` if the location tracking should be visualized, `false` otherwise.
 */
data class ShowLocationTrackingStatusChangedEvent(val trackingEnabled: Boolean)