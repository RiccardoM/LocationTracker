package it.riccardomontagnin.locationtracker.usecase

import io.reactivex.Completable
import io.reactivex.Single
import kotlin.reflect.KClass

/**
 * Interface used in order to simply work with the settings.
 */
interface SettingsRepository {
    /**
     * Saves a setting.
     * @param name: Name of the setting to save.
     * @param value: Value of the setting to save.
     * @return Returns a [Completable] that completes with no errors iff the setting has been saved
     * correctly, or emits any error otherwise.
     */
    fun <T: Any> saveSetting(name: String, value: T): Completable

    /**
     * Retrieves the value of a setting if the given settings has been saved, or a default value
     * otherwise.
     * @param name: Name of the setting to retrieve.
     * @param type: Type of the setting value that should be retrieved.
     * @param defaultValue: Default value that should be emitted if the setting has not been found.
     * @return Returns a [Single] that emits the saved value if the setting has been found, emits
     * the default value otherwise, of emits any error that can be raise.
     */
    fun <T: Any> getSettingValue(name: String, type: KClass<T>, defaultValue: T): Single<T>

    companion object {
        const val SETTING_TRACKING_ENABLED = "tracking enabled"
    }
}