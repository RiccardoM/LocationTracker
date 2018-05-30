package it.riccardomontagnin.locationtracker.repository.settings

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import it.riccardomontagnin.locationtracker.usecase.SettingsRepository
import javax.inject.Inject
import kotlin.reflect.KClass

class SettingsRepositoryImpl @Inject constructor(private val context: Context): SettingsRepository {

    private val gson = Gson()

    override fun <T : Any> saveSetting(name: String, value: T): Completable {
        return Completable.create { emitter ->
            // Get the Shared preferences
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            // Put inside the SharedPreferences the value that has been sent, after serializing it
            // with Gson in order to transform it into a String
            sharedPreferences.edit()
                    .putString(name, gson.toJson(value))
                    .apply()

            emitter.onComplete()
        }
    }

    override fun <T : Any> getSettingValue(name: String, type: KClass<T>, defaultValue: T): Single<T> {
        return Single.create { emitter ->
            try {
                // Get the SharedPreferences
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

                // Check if the preferences contain the given name
                if (!sharedPreferences.contains(name)){
                    // If they do not, then emit the default value
                    emitter.onSuccess(defaultValue)
                } else {
                    // If there is the key, get the value as a String
                    val value = sharedPreferences.getString(name, "{}")

                    // Emit the value after parsing it with Gson in order to get back the required
                    // type
                    emitter.onSuccess(gson.fromJson(value, type.java))
                }

            } catch (exception: Exception) {
                // Emit any exception that might raise
                emitter.onError(exception)
            }
        }
    }
}