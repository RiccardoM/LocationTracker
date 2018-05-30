package it.riccardomontagnin.locationtracker.pages.main.presenter

import net.grandcentrix.thirtyinch.TiView

/**
 * Represents the main view of the application, from which the user can reach all the other views.
 */
interface MainView: TiView {

    fun requestPermissions()

    /**
     * Shows the popup telling the user if the location tracking is in progress or not.
     * @param locationInProgress: `true` if the location tracking has been enabled, `false` if the
     * tracking has been disables.
     */
    fun setLocationTrackingEnabledPopup(locationInProgress: Boolean)

    /**
     * Shows the journey view representing all the data about the current journey of the user.
     */
    fun showJourneyView()

    /**
     * Sets if the real time visualization of the journey the user is doing should be made visible
     * or not.
     * @param enabled: `true` if the user should see real time the journey he is doing, `false`
     * otherwise.
     */
    fun setJourneyInProgressVisible(enabled: Boolean)

    /**
     * Shows the view inside which the user can see all the journeys he has done in the past.
     */
    fun showJourneyListView()
}