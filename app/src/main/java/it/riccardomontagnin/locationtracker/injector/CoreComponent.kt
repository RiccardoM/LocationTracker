package it.riccardomontagnin.locationtracker.injector

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import it.riccardomontagnin.locationtracker.usecase.JourneyRepository
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import it.riccardomontagnin.locationtracker.usecase.SettingsRepository

/**
 * Dagger Component used to provide basic dependencies that can be reached from anywhere and should
 * be singleton instances.
 */
@Component(modules = [RepositoryModule::class])
interface CoreComponent {

    /**
     * Exposes a singleton [LocationRepository] instance.
     */
    fun locationRepository(): LocationRepository

    /**
     * Exposes a singleton [SettingsRepository] instance.
     */
    fun settingsRepository(): SettingsRepository

    /**
     * Exposes a singleton [JourneyRepository] instance.
     */
    fun journeyRepository(): JourneyRepository

    /**
     * Custom component builder used in order to bind custom object that could not be bounded
     * otherwise.
     */
    @Component.Builder
    interface Builder {

        /**
         * Binds the given [Context] instance in order to make it injectable.
         * Everytime an object will require a [Context] instance using Dagger, then it will return
         * [context].
         */
        @BindsInstance fun setContext(context: Context): Builder

        /**
         * Builds the component.
         */
        fun build(): CoreComponent
    }

}