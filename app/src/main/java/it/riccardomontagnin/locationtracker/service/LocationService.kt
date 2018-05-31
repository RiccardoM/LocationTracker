package it.riccardomontagnin.locationtracker.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.HandlerThread
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import it.riccardomontagnin.locationtracker.model.LocationData
import it.riccardomontagnin.locationtracker.model.event.LocationUpdateEvent
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.util.*


/**
 * Service used in order to periodically retrieve the user location even if the application is in
 * background.
 *
 * In order to start this service, the following code should be put inside an Activity:
 * <code>
 *     startService(Intent(this, LocationService::class.java))
 * </code>
 *
 * If the service should be stopped, on the other hand, the following code should be used:
 * <code>
 *      stopService(Intent(this, LocationService::class.java))
 * </code>
 */
class LocationService : Service() {

    /**
     * Object that lets us retrieve the position of the user periodically.
     * In order to work properly, the {@link android.Manifest.permission#ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_FINE_LOCATION} should be granted.
     */
    private var locationManager: LocationManager? = null

    /**
     * List of locations managers that should be used, ordered by importance.
     * If the top one cannot be used, than the second one is used as fallback.
     */
    private val locationManagers = listOf (
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER
    )

    /**
     * List of location listeners that will be used in order to fetch the user position constantly.
     */
    private val locationListeners = arrayOf(
            LocationListener(locationManagers[0]),
            LocationListener(locationManagers[1])
    )


    /**
     * As this service won't be bound to anything, we just return null.
     */
    override fun onBind(intent: Intent): IBinder? = null

    /**
     * Method called when the service is started.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Location service started")
        startForeground()
        startLocationTracking()
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    private fun startLocationTracking() {
        // Get the location manager so that we can get the user location
        if (locationManager == null) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        val gpsEnabled = checkGpsPermission(this)
        val networkEnabled = checkNetworkPermission(this)

        if (gpsEnabled || networkEnabled) {
            val locationClient = LocationServices.getFusedLocationProviderClient(this)
            locationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) postLocation(location)
            }
        }

        // If the user has granted the permission to use the GPS, than track the position with it
        if (gpsEnabled) {
            val thread = HandlerThread("GPSLocationThread")
            thread.start()
            locationManager?.requestLocationUpdates(locationManagers[0],
                    LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0],
                    thread.looper
            )
        }

        // Otherwise, it the user has granted the permission to user the network, than track the
        // position with it
        else if (networkEnabled){
            val thread = HandlerThread("NetworkLocationThread")
            thread.start()
            locationManager?.requestLocationUpdates(locationManagers[1],
                    LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]
            )
        }
    }

    private fun startForeground() {
        // Set it foreground
        //        val notificationIntent = Intent(this, MainActivity::class.java)
        //
        //        val pendingIntent = PendingIntent.getActivity(this, 0,
        //                notificationIntent, 0)
        //
        //        val notification = NotificationCompat.Builder(this, "")
        ////                .setSmallIcon(R.mipmap.ic_launcher)
        //                .setContentTitle("My Awesome App")
        //                .setContentText("Doing some work...")
        //                .setContentIntent(pendingIntent).build()
        //
        //        startForeground(1337, notification)
    }

    /**
     * Checks for the network position.
     * @return Returns `true` if the user has granted the permission, `false` otherwise.
     */
    private fun checkNetworkPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if the user has granted the permission to user the GPS in order to track his position.
     * @return Returns `true` if the user has granted the permission, `false` otherwise.
     */
    private fun checkGpsPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * Posts the location on the application's [EventBus] instance notifying every listener
     * about the available update.
     */
    internal fun postLocation(location: Location) {
        Timber.d("New location: $location")

        // Check to be sure that this location is not fake
        if (location.latitude != 0.0 && location.longitude != 0.0) {
            // Post the location update to EventBus in order to notify the observers
            EventBus.getDefault().postSticky(LocationUpdateEvent(LocationData(
                    longitude = location.longitude,
                    latitude = location.latitude,
                    date = Date()
            )))
        }
    }

    /**
     * Calles when the service is destroyed, it performs all the cleanup that should be done.
     */
    override fun onDestroy() {
        Timber.d("Location service stopped")
        super.onDestroy()

        // Remove all the updates listeners from the location manager by checking though all the
        // possible location listeners and removing them if they have been attached to the
        // LocationManager
        for (i in locationListeners.indices) {
            locationManager?.removeUpdates(locationListeners[i])
        }
    }


    /**
     * Utility class that extends the [LocationListener] and is used in order to listen to location
     * changes using the given `provider`.
     * Everytime the location changes, the [onLocationChanged] method is called, and will notify all
     * the object that are watching it.
     */
    private inner class LocationListener(provider: String) : android.location.LocationListener {

        init {
            // Start by posting the current location of the user
            postLocation(Location(provider))
        }

        override fun onLocationChanged(location: Location) {
            Timber.d("New location: $location")

            // Every time a new location is retrieved, post it to everyone who is listening
            postLocation(location)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    }



    companion object {
        /**
         * Minimum interval between one location scan and the other, in milliseconds.
         */
        private const val LOCATION_INTERVAL = 30_000L

        /**
         * Minimum distance, in meters, that should change from one location scan to the other in order to
         * signal the location listeners about a new position.
         */
        private const val LOCATION_DISTANCE = 0f
    }

}