// app/src/main/java/de/yanneckreiss/mlkittutorial/viewmodel/HistoryViewModel.kt
package de.yanneckreiss.mlkittutorial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.yanneckreiss.mlkittutorial.data.HistoryDataStore
import de.yanneckreiss.mlkittutorial.data.HistoryEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HistoryDataStore(app.applicationContext)

    val historyList = repo.historyFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun insert(text: String) = viewModelScope.launch { repo.add(text) }
    fun update(entry: HistoryEntry) = viewModelScope.launch {
        repo.update(entry.id, entry.text)
    }
    fun clearAll()          = viewModelScope.launch { repo.clear() }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory =
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>) =
                    HistoryViewModel(app) as T
            }
    }
}
