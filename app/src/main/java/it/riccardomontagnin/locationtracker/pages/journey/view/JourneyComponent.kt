package it.riccardomontagnin.locationtracker.pages.journey.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

/**
 * Dagger [Component] used in order to inject the dependencies inside [JourneyFragment] instances.
 */
@Component(dependencies = [CoreComponent::class])
interface JourneyComponent {

    /**
     * Inject the dependencies inside the given [JourneyComponent] instance.
     */
    fun inject(view: JourneyFragment)

}