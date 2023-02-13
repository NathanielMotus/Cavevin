package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.BottleRepository
import com.nathaniel.motus.cavevin.data.BottleTypeRepository
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BottleEditViewModel(application: Application) : AndroidViewModel(application) {

    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(application))
    private val bottleTypeRepository = BottleTypeRepository(CellarDatabase.getDatabase(application))
    var bottleID = 1

    private var _bottleAppellation = MutableLiveData("")
    val bottleAppellation: LiveData<String> get() = _bottleAppellation


    private suspend fun updateBottleAppellation() {
        _bottleAppellation.value = bottleRepository.findBottleById(bottleID).appellation
    }

    fun updateBottleEditViewModel() {
        viewModelScope.launch {
            updateBottleAppellation()
        }
    }

}

class BottleEditViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BottleEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BottleEditViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}