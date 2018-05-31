package it.riccardomontagnin.locationtracker.pages.journey.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.application.LocationTrackerApp
import it.riccardomontagnin.locationtracker.injector.CoreComponent
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.model.event.JourneyStatusChangedEvent
import it.riccardomontagnin.locationtracker.model.event.ShowLocationTrackingStatusChangedEvent
import it.riccardomontagnin.locationtracker.pages.journey.presenter.JourneyPresenter
import it.riccardomontagnin.locationtracker.pages.journey.presenter.JourneyView
import net.grandcentrix.thirtyinch.TiFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject




/**
 * Fragment representing the view that the user sees when he wants to visualize its current location
 * or when a new journey is in progress and he wants to visualize the path he has followed.
 */
class JourneyFragment: TiFragment<JourneyPresenter, JourneyView>(), JourneyView, OnMapReadyCallback {

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private val journeyPoints: MutableList<LatLng> = mutableListOf()

    @Inject lateinit var presenter: JourneyPresenter

    override fun providePresenter(): JourneyPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inject dependencies
        DaggerJourneyComponent.builder()
                .coreComponent(LocationTrackerApp.getComponent(CoreComponent::class))
                .build().inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map_map_view)
        mapView?.getMapAsync(this)
        mapView?.onCreate(savedInstanceState)

        return view
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    @Subscribe(sticky = true)
    internal fun locationTrackingStatusListener(event: ShowLocationTrackingStatusChangedEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        presenter.setLocationTrackingEnabled(event.trackingEnabled)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        // Save the GoogleMap instance so we can later add markers or track the user journey on it
        this.googleMap = googleMap
    }

    override fun showCurrentLocationMapPlaceholder(location: LocationData) {
        // Create the LatLong object in order to represent a location on the map
        val locationData = LatLng(location.latitude, location.longitude)

        // Create a marker representing the current location of the user
        val marker = MarkerOptions()
                .title(getString(R.string.map_marker_last_known_location))
                .position(locationData)

        // Clear all the markers already present
        googleMap?.clear()

        // Add the new marker to the map
        googleMap?.addMarker(marker)

        // Zoom to better show the current location of the user
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(locationData, 15.0f))
    }

    override fun addLocationToCurrentJourney(location: LocationData) {
        val point = LatLng(location.latitude, location.longitude)
        journeyPoints.add(point)

        redrawPath(location)
    }

    private fun redrawPath(location: LocationData? = null) {
        // Clear all the markers and lines
        googleMap?.clear()

        if (location != null) {
            // Show the current location
            showCurrentLocationMapPlaceholder(location)

            // Create the polyline object to represent the path
            val polyline = PolylineOptions()
                    .width(5f)
                    .color(ContextCompat.getColor(activity!!, R.color.colorPrimary))
                    .geodesic(true)

            // Add all the points to the path
            journeyPoints.forEach { polyline.add(it) }

            // Add the path to the map
            googleMap?.addPolyline(polyline)
        }

    }

    override fun clearJourney() {
        val lastLocation = journeyPoints
                .lastOrNull()
                ?.let { LocationData(it.longitude, it.latitude) }

        journeyPoints.clear()
        redrawPath(lastLocation)
    }

    override fun showJourneyStartedPopup() {
        Toast.makeText(activity!!, R.string.popup_journey_started, Toast.LENGTH_LONG).show()
    }

    override fun showJourneyEndedPopup() {
        EventBus.getDefault().postSticky(JourneyStatusChangedEvent(false))
        Toast.makeText(activity!!, R.string.popup_journey_ended, Toast.LENGTH_LONG).show()
    }

}