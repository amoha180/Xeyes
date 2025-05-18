package de.yanneckreiss.mlkittutorial.viewmodel

import android.app.Application
import androidx.lifecycle.*
import de.yanneckreiss.mlkittutorial.data.HistoryDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HistoryDataStore(app.applicationContext)

    /** Expose the Flow as StateFlow for Compose */
    val historyList = repo.historyFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun insert(text: String) = viewModelScope.launch { repo.add(text) }
    fun clearAll()          = viewModelScope.launch { repo.clear()  }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory =
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>) =
                    HistoryViewModel(app) as T
            }
    }
}
