package it.riccardomontagnin.locationtracker.injector

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import it.riccardomontagnin.locationtracker.usecase.LocationRepository
import it.riccardomontagnin.locationtracker.usecase.SettingsRepository

@Component(modules = [RepositoryModule::class])
interface CoreComponent {

    fun locationRepository(): LocationRepository
    fun settingsRepository(): SettingsRepository

    @Component.Builder
    interface Builder {
        @BindsInstance fun setContext(context: Context): Builder

        fun build(): CoreComponent
    }

}