package de.yanneckreiss.mlkittutorial.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ① Extension property to get DataStore<Preferences>
private val Context.dataStore by preferencesDataStore(name = "history_prefs")

class HistoryDataStore(private val context: Context) {
    private val gson = Gson()
    private val HISTORY_KEY = stringPreferencesKey("history_list")

    /** A Flow of the list of strings, newest first */
    val historyFlow: Flow<List<String>> = context.dataStore.data
        .map { prefs ->
            val json = prefs[HISTORY_KEY] ?: "[]"
            // parse JSON array into List<String>
            gson.fromJson<List<String>>(json, object: TypeToken<List<String>>(){}.type)
        }

    /** Add a new entry at the front */
    suspend fun add(text: String) {
        context.dataStore.edit { prefs ->
            val json = prefs[HISTORY_KEY] ?: "[]"
            val list = gson.fromJson<MutableList<String>>(json, object: TypeToken<MutableList<String>>(){}.type)
            list.add(0, text)
            prefs[HISTORY_KEY] = gson.toJson(list)
        }
    }

    /** Wipe history */
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs[HISTORY_KEY] = "[]"
        }
    }
}
