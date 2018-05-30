package it.riccardomontagnin.locationtracker.pages.main.view

import dagger.Component
import it.riccardomontagnin.locationtracker.injector.CoreComponent

@Component(dependencies = [CoreComponent::class])
interface MainComponent {
    fun inject(view: MainActivity)
}