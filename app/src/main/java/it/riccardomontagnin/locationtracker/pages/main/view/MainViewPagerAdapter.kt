package it.riccardomontagnin.locationtracker.pages.main.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import it.riccardomontagnin.locationtracker.pages.journey.view.JourneyFragment
import it.riccardomontagnin.locationtracker.pages.journey_list.view.JourneyListFragment

/**
 * Class representing the adapter that is used inside the main view in order to provide the tabs
 * that the user can browse.
 * @see FragmentPagerAdapter
 */
class MainViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    /**
     * @inheritDoc
     */
    override fun getCount(): Int = 2

    /**
     * @inheritDoc
     */
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> JourneyFragment()
        else -> JourneyListFragment()
    }

}