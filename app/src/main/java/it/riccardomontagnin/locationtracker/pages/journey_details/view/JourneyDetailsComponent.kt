package it.riccardomontagnin.locationtracker.pages.journey_details.view

import dagger.BindsInstance
import dagger.Component
import it.riccardomontagnin.locationtracker.model.JourneyData

@Component
interface JourneyDetailsComponent {
    fun inject(view: JourneyDetailsActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance fun setJourney(journey: JourneyData): Builder
        fun build(): JourneyDetailsComponent
    }

}