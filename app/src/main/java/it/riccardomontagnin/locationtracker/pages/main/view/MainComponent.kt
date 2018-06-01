package it.riccardomontagnin.locationtracker.pages.main.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

/**
 * Dagger [Component] that will be used in order to inject dependencies inside [MainActivity]
 * instances.
 */
@Component(dependencies = [CoreComponent::class])
interface MainComponent {

    /**
     * Injects the required dependencies inside the given view.
     * @param view: Instance of [MainActivity] inside which the dependencies should be injected.
     */
    fun inject(view: MainActivity)

}