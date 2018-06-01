package it.riccardomontagnin.locationtracker.model.event

/**
 * Data class representing the event that is emitted when the journey status changes.
 * @param journeyInProgress: If `true` tells that the journey has been started, if `false` it tells
 * that the journey has been stopped.
 */
data class JourneyStatusChangedEvent(val journeyInProgress: Boolean)