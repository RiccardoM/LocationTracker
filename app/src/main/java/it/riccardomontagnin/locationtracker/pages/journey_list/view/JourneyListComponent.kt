package it.riccardomontagnin.locationtracker.pages.journey_list.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

@Component(dependencies = [CoreComponent::class])
interface JourneyListComponent {
    fun inject(view: JourneyListFragment)
}