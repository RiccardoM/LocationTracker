package it.riccardomontagnin.locationtracker.pages.main.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import it.riccardomontagnin.locationtracker.pages.journey_list.view.JourneyListFragment
import it.riccardomontagnin.locationtracker.pages.journey.view.JourneyFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> JourneyFragment()
        else -> JourneyListFragment()
    }

}