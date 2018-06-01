package it.riccardomontagnin.locationtracker.pages.journey_list.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

/**
 * Dagger [Component] that will be used in order to inject dependencies inside [JourneyListFragment]
 * instances.
 */
@Component(dependencies = [CoreComponent::class])
interface JourneyListComponent {

    /**
     * Injects the required dependencies inside the given view.
     * @param view: Instance of [JourneyListFragment] inside which the dependencies should be injected.
     */
    fun inject(view: JourneyListFragment)

}