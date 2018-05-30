package it.riccardomontagnin.locationtracker.application

import android.app.Application
import it.riccardomontagnin.locationtracker.BuildConfig
import it.riccardomontagnin.locationtracker.injector.CoreComponent
import it.riccardomontagnin.locationtracker.injector.DaggerCoreComponent
import timber.log.Timber
import kotlin.reflect.KClass

class LocationTrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG){
            // Start the debug logging if the application is in debug mode
            Timber.plant(Timber.DebugTree())
        }

        // Add  the CoreComponent instance as a singleton component
        addComponent(CoreComponent::class, DaggerCoreComponent.builder()
                .setContext(this)
                .build())
    }

    companion object {

        /**
         * Map containing all the components that should provide singleton instances of their
         * exposed objects.
         */
        private val singletonComponents = mutableMapOf<Class<out Any>, Any>()

        /**
         * Adds a component to the map of singleton components.
         * @param clazz: Class of the component to add.
         * @param instance: Instance of the component to add that will be later retrieved and used.
         */
        fun <T: Any> addComponent (clazz : KClass<T>, instance : T) {
            singletonComponents[clazz.java] = instance
        }

        /**
         * Retrieves the instance of the component with the given class type.
         * @param clazz Type of the component which instance should be retrieved.
         * @return Returns the instance of the component with the given class type, if found.
         * @throws UnsupportedOperationException if no component with the given class has been found.
         */
        @Suppress("UNCHECKED_CAST")
        fun <T: Any> getComponent (clazz : KClass<T>) : T {
            if (!singletonComponents.containsKey(clazz.java)) {
                val message = "Cannot retrieve component of class $clazz.\n" +
                        "Please be sure you have called CoreApplication.addComponent."
                throw UnsupportedOperationException(message)
            }
            return singletonComponents[clazz.java] as T
        }

    }

}