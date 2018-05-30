package it.riccardomontagnin.locationtracker.pages.map.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

@Component(dependencies = [CoreComponent::class])
interface JourneyComponent {
    fun inject(view: JourneyFragment)
}