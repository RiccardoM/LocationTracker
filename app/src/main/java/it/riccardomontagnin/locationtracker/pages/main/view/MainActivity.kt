package it.riccardomontagnin.locationtracker.pages.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import it.riccardomontagnin.locationtracker.R
import it.riccardomontagnin.locationtracker.application.LocationTrackerApp
import it.riccardomontagnin.locationtracker.injector.CoreComponent
import it.riccardomontagnin.locationtracker.model.event.ShowLocationTrackingStatusChangedEvent
import it.riccardomontagnin.locationtracker.pages.main.presenter.MainPresenter
import it.riccardomontagnin.locationtracker.pages.main.presenter.MainView
import net.grandcentrix.thirtyinch.TiActivity
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * Implementation of [MainView].
 * @see MainView
 */
class MainActivity : TiActivity<MainPresenter, MainView>(), MainView {

    private lateinit var fab: FloatingActionButton
    private lateinit var tabLayout: TabLayout

    @Inject lateinit var presenter: MainPresenter

    /**
     * @inheritDoc
     */
    override fun providePresenter(): MainPresenter = presenter

    /**
     * @inheritDoc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Inject the dependencies
        DaggerMainComponent.builder()
                .coreComponent(LocationTrackerApp.getComponent(CoreComponent::class))
                .build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(TAB_TITLES[0])

        // Setup the FloatingActionButton so that when the user clicks it, it triggers the
        // location tracking
        fab = findViewById(R.id.main_fab)
        fab.setOnClickListener { presenter.trackButtonClicked() }

        // Setup ViewPager
        val viewPager = findViewById<ViewPager>(R.id.main_viewpager)
        viewPager.adapter = MainViewPagerAdapter(supportFragmentManager)

        // Change the action bar title based on the currently selected tab
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                supportActionBar?.title = getString(TAB_TITLES[position])
            }
        })

        // Setup TabLayout to work with ViewPager
        tabLayout = findViewById(R.id.main_tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        // Set icons
        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)?.setIcon(TAB_ICONS[i])

            val icon = tabLayout.getTabAt(i)?.icon
            val colors = ContextCompat.getColorStateList(this, R.color.tab_bar_icon_color)
            DrawableCompat.setTintList(icon!!, colors)
        }

    }

    /**
     * @inheritDoc
     */
    override fun requestPermissions() {
        // Simple inline function to check if a permissions has been granted or not
        // @param permission: String representing the permission to check
        fun checkPermission(permission: String): Boolean {
            return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Check for all the permissions to be granted by the user
        val permissionGranted = permissions.map { checkPermission(it) }.reduce { a, b -> a && b }
        presenter.setLocationActivated(permissionGranted)

        // Check if the fine location permission has been granted
        if (!permissionGranted) {
            // If it has not, then request them
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    /**
     * @inheritDoc
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // Check that the results are not empty and that the permission has been granted
                val permissionAccepted = grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED

                // Notify the presenter
                presenter.setLocationActivated(permissionAccepted)
            }
        }
    }

    /**
     * @inheritDoc
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * @inheritDoc
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * @inheritDoc
     */
    override fun showJourneyView() {
        // Shows the first tab, which is the one with the journey view inside
        tabLayout.getTabAt(0)?.select()
    }

    /**
     * @inheritDoc
     */
    override fun setJourneyInProgress(enabled: Boolean) {
        // Create the object representing the even that tells the real time journey should be
        // displayed
        val event = ShowLocationTrackingStatusChangedEvent(enabled)

        // Post the event
        EventBus.getDefault().postSticky(event)

        // Update the FloatingActionButton
        val icon = when(enabled) {
            true -> R.drawable.ic_location_turn_off
            false -> R.drawable.ic_location_turn_on
        }
        fab.setImageResource(icon)
    }


    companion object {

        /**
         * Number of the request code that is sent in order to ask the user for permission to track
         * his location. The random value should ensure that no other request coming has the same
         * code.
         */
        private const val PERMISSION_REQUEST_CODE = 156

        /**
         * List of icons that should be displayed instead of the tab titles.
         */
        private val TAB_ICONS = listOf(R.drawable.ic_tab_map, R.drawable.ic_tab_journey_list)

        /**
         * List of title that are associated with the tabs present inside the view and that will
         * be used as the activity name when the user scrolls through the tabs.
         */
        private val TAB_TITLES = listOf(R.string.tab_title_map, R.string.tab_title_journey_list)

    }

}
