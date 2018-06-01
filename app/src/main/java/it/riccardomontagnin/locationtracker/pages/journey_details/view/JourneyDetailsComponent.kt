package it.riccardomontagnin.locationtracker.pages.journey_details.view

import dagger.BindsInstance
import dagger.Component
import it.riccardomontagnin.locationtracker.model.JourneyData

/**
 * Dagger [Component] used in order to inject the dependencies inside [JourneyDetailsActivity] instances.
 */
@Component
interface JourneyDetailsComponent {

    /**
     * Inject the dependencies inside the given [JourneyDetailsActivity] instance.
     */
    fun inject(view: JourneyDetailsActivity)

    /**
     * Interface used in order to create the object that will create the [JourneyDetailsComponent]
     * instances.
     */
    @Component.Builder
    interface Builder {
        /**
         * Binds [JourneyData] instances with [journey] in order to inject it everytime an object
         * asks for a [JourneyData] instance as a dependency.
         */
        @BindsInstance fun setJourney(journey: JourneyData): Builder

        /**
         * Builds the component instance.
         */
        fun build(): JourneyDetailsComponent
    }

}