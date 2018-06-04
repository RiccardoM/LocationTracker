package it.riccardomontagnin.locationtracker.pages.journey_details.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.model.JourneyData
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.pages.journey_details.presenter.JourneyDetailsPresenter
import it.riccardomontagnin.locationtracker.pages.journey_details.presenter.JourneyDetailsView
import it.riccardomontagnin.locationtracker.toBitmapDescriptor
import net.grandcentrix.thirtyinch.TiActivity
import javax.inject.Inject


/**
 * Activity representing the implementation of [JourneyDetailsView].
 * @see JourneyDetailsView
 */
class JourneyDetailsActivity: TiActivity<JourneyDetailsPresenter, JourneyDetailsView>(),
        OnMapReadyCallback, JourneyDetailsView {

    @Inject lateinit var presenter: JourneyDetailsPresenter

    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var averageSpeedTextView: TextView
    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null

    /**
     * @inheritDoc
     */
    override fun providePresenter(): JourneyDetailsPresenter  = presenter

    /**
     * @inheritDoc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerJourneyDetailsComponent.builder()
                .setJourney(intent.getParcelableExtra(JOURNEY_KEY))
                .build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_details)

        // Setup the MapView
        mapView = findViewById(R.id.journey_details_map_view)
        mapView?.getMapAsync(this)
        mapView?.onCreate(savedInstanceState)

        // Setup all the TextViews
        startTimeTextView = findViewById(R.id.journey_details_start_time)
        endTimeTextView = findViewById(R.id.journey_details_end_time)
        distanceTextView = findViewById(R.id.journey_details_distance)
        durationTextView = findViewById(R.id.journey_details_duration)
        averageSpeedTextView = findViewById(R.id.journey_details_average_speed)

    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    /**
     * @inheritDoc
     */
    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    /**
     * @inheritDoc
     */
    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    /**
     * @inheritDoc
     */
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    /**
     * @inheritDoc
     */
    override fun setStartTime(starTime: String) {
        startTimeTextView.text = starTime
    }

    /**
     * @inheritDoc
     */
    override fun setEndTime(endTime: String) {
        endTimeTextView.text = endTime
    }

    /**
     * @inheritDoc
     */
    override fun setDistance(distance: Double) {
        distanceTextView.text = getString(R.string.duration_meters, distance)
    }

    /**
     * @inheritDoc
     */
    override fun setDuration(duration: Long) {
        // Compute the seconds, minutes and hours values from the milliseconds duration value
        val seconds = (duration / 1000).toInt() % 60
        val minutes = (duration / (1000 * 60) % 60).toInt()
        val hours = (duration / (1000 * 60 * 60) % 24).toInt()

        // Build a string representing an easy-to-read representation of the values computed before
        val stringBuilder = StringBuilder()
        if (hours > 0) stringBuilder.append(getString(R.string.hours, hours)).append(" ")
        if (hours > 0 || minutes > 0) stringBuilder.append(getString(R.string.minutes, minutes)).append(" ")
        stringBuilder.append(getString(R.string.seconds, seconds))

        // Display the built string
        durationTextView.text = stringBuilder.toString()
    }

    /**
     * @inheritDoc
     */
    override fun setAverageSpeed(averageSpeed: Double) {
        averageSpeedTextView.text = getString(R.string.speed_km_h, averageSpeed)
    }

    /**
     * @inheritDoc
     */
    override fun drawLocations(locations: List<LocationData>) {
        // Che to be sure that there is at least one location tracked during the journey
        if (locations.isNotEmpty()) {
            // Clear the map from the previous path, if any exist
            googleMap?.clear()

            // Create the marker that displays the position of the first location tracked
            val startMarker = MarkerOptions()
                    .title(getString(R.string.journey_details_start_position))
                    .position(locations.first().let { LatLng(it.latitude, it.longitude) })
                    .icon(R.drawable.ic_position_start.toBitmapDescriptor(this))
            googleMap?.addMarker(startMarker)

            // Create the marker that displays the last location tracked
            val endLocation = locations.last().let { LatLng(it.latitude, it.longitude) }
            val endMarker = MarkerOptions()
                    .title(getString(R.string.journey_details_end_position))
                    .position(endLocation)
                    .icon(R.drawable.ic_position_end.toBitmapDescriptor(this))
            googleMap?.addMarker(endMarker)

            // Zoom the view to the end location
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(endLocation, 15.0f))

            // Create the polyline that will be used to represent the journey path
            val polyline = PolylineOptions()
                    .width(5f)
                    .color(ContextCompat.getColor(this, R.color.colorPrimary))
                    .geodesic(true)

            // Add all the locations to the path
            locations.forEach { polyline.add(LatLng(it.latitude, it.longitude)) }

            // Show the path inside the map
            googleMap?.addPolyline(polyline)

        }
    }

    /**
     * @inheritDoc
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        // Save the GoogleMap instance so we can later add markers or track the user journey on it
        this.googleMap = googleMap

        // Notify the presenter that the map is ready to be used
        presenter.mapReady()
    }

    companion object {
        private const val JOURNEY_KEY = "journey"

        /**
         * Starts the activity passing to the new instance the journey data that should be displayed
         * inside it. This is done in order to avoid errors on keys when passing the data using intents.
         */
        fun start(activity: Activity, journeyData: JourneyData) {
            // Create the intent
            val intent = Intent(activity, JourneyDetailsActivity::class.java)

            // Put inside it the journey details
            intent.putExtra(JOURNEY_KEY, journeyData)

            // Start the activity
            activity.startActivity(intent)
        }
    }

}