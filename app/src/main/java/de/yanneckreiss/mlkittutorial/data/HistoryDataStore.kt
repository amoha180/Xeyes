// app/src/main/java/de/yanneckreiss/mlkittutorial/data/HistoryDataStore.kt
package de.yanneckreiss.mlkittutorial.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.dataStore by preferencesDataStore("history_prefs")

class HistoryDataStore(context: Context) {
    private val ds = context.dataStore
    private val gson = Gson()
    private val KEY = stringPreferencesKey("history_list")

    /** Flow of all entries, newest‐first */
    val historyFlow: Flow<List<HistoryEntry>> = ds.data
        .map { prefs ->
            val json = prefs[KEY] ?: "[]"
            gson.fromJson(
                json,
                object: TypeToken<List<HistoryEntry>>(){}.type
            )
        }

    /** Add new entry */
    suspend fun add(text: String) {
        ds.edit { prefs ->
            val list: MutableList<HistoryEntry> = gson.fromJson(
                prefs[KEY] ?: "[]",
                object: TypeToken<MutableList<HistoryEntry>>(){}.type
            )
            list.add(0, HistoryEntry(UUID.randomUUID().toString(), text))
            prefs[KEY] = gson.toJson(list)
        }
    }

    /** Update an existing entry’s text */
    suspend fun update(id: String, newText: String) {
        ds.edit { prefs ->
            val list: MutableList<HistoryEntry> = gson.fromJson(
                prefs[KEY] ?: "[]",
                object: TypeToken<MutableList<HistoryEntry>>(){}.type
            )
            val idx = list.indexOfFirst { it.id == id }
            if (idx >= 0) {
                list[idx] = list[idx].copy(text = newText)
                prefs[KEY] = gson.toJson(list)
            }
        }
    }

    /** Clear all */
    suspend fun clear() = ds.edit { it[KEY] = "[]" }
}
