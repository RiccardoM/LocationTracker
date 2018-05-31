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

    override fun providePresenter(): JourneyDetailsPresenter  = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerJourneyDetailsComponent.builder()
                .setJourney(intent.getParcelableExtra(JOURNEY_KEY))
                .build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_details)

        mapView = findViewById(R.id.journey_details_map_view)
        mapView?.getMapAsync(this)
        mapView?.onCreate(savedInstanceState)

        startTimeTextView = findViewById(R.id.journey_details_start_time)
        endTimeTextView = findViewById(R.id.journey_details_end_time)
        distanceTextView = findViewById(R.id.journey_details_distance)
        durationTextView = findViewById(R.id.journey_details_duration)
        averageSpeedTextView = findViewById(R.id.journey_details_average_speed)

    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
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

    override fun setStartTime(starTime: String) {
        startTimeTextView.text = starTime
    }

    override fun setEndTime(endTime: String) {
        endTimeTextView.text = endTime
    }

    override fun setDistance(distance: Double) {
        distanceTextView.text = getString(R.string.duration_meters, distance)
    }

    override fun setDuration(duration: Long) {
        val seconds = (duration / 1000).toInt() % 60
        val minutes = (duration / (1000 * 60) % 60).toInt()
        val hours = (duration / (1000 * 60 * 60) % 24).toInt()

        val stringBuilder = StringBuilder()
        if (hours > 0) stringBuilder.append(getString(R.string.hours, hours)).append(" ")
        if (hours > 0 || minutes > 0) stringBuilder.append(getString(R.string.minutes)).append(" ")
        stringBuilder.append(getString(R.string.seconds, seconds))

        durationTextView.text = stringBuilder.toString()
    }

    override fun setAverageSpeed(averageSpeed: Double) {
        averageSpeedTextView.text = getString(R.string.speed_km_h, averageSpeed)
    }

    override fun drawLocations(locations: List<LocationData>) {
        googleMap?.clear()

        if (locations.isNotEmpty()) {
            val startMarker = MarkerOptions()
                    .title(getString(R.string.journey_details_start_position))
                    .position(locations.first().let { LatLng(it.latitude, it.longitude) })
                    .icon(R.drawable.ic_position_start.toBitmapDescriptor(this))
            googleMap?.addMarker(startMarker)


            val endLocation = locations.last().let { LatLng(it.latitude, it.longitude) }
            val endMarker = MarkerOptions()
                    .title(getString(R.string.journey_details_end_position))
                    .position(endLocation)
                    .icon(R.drawable.ic_position_end.toBitmapDescriptor(this))

            googleMap?.addMarker(endMarker)
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(endLocation, 15.0f))

            val polyline = PolylineOptions()
                    .width(5f)
                    .color(ContextCompat.getColor(this, R.color.colorPrimary))
                    .geodesic(true)

            // Add all the locations to the path
            locations.forEach { polyline.add(LatLng(it.latitude, it.longitude)) }
            googleMap?.addPolyline(polyline)

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        // Save the GoogleMap instance so we can later add markers or track the user journey on it
        this.googleMap = googleMap
        presenter.mapReady()
    }

    companion object {
        private const val JOURNEY_KEY = "journey"

        fun start(activity: Activity, journeyData: JourneyData) {
            val intent = Intent(activity, JourneyDetailsActivity::class.java)
            intent.putExtra(JOURNEY_KEY, journeyData)
            activity.startActivity(intent)
        }
    }

}