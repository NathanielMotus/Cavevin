package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BottleDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(application))
    private val bottleTypeRepository = BottleTypeRepository(CellarDatabase.getDatabase(application))
    private val wineColorRepository = WineColorRepository(CellarDatabase.getDatabase(application))
    private val wineStillnessRepository =
        WineStillnessRepository(CellarDatabase.getDatabase(application))

    //**********************************
    //State
    //**********************************
    var bottleId = 1

    private var _appellation = MutableLiveData("")
    val appellation: LiveData<String>
        get() = _appellation

    private var _domain = MutableLiveData("")
    val domain: LiveData<String>
        get() = _domain

    private var _cuvee = MutableLiveData("")
    val cuvee: LiveData<String>
        get() = _cuvee

    private var _vintage = MutableLiveData("")
    val vintage: LiveData<String>
        get() = _vintage

    private var _bottleTypeAndCapacity = MutableLiveData("")
    val bottleTypeAndCapacity: LiveData<String>
        get() = _bottleTypeAndCapacity

    private var _origin = MutableLiveData("")
    val origin: LiveData<String>
        get() = _origin

    private var _comment = MutableLiveData("")
    val comment: LiveData<String>
        get() = _comment

    private var _price=MutableLiveData("")
    val price:LiveData<String>
        get() = _price

    private var _agingCapacity=MutableLiveData("")
    val agingCapacity:LiveData<String>
        get() = _agingCapacity

    //************************************
    //update
    //************************************
    private suspend fun updateAppellation() {
        _appellation.value = bottleRepository.findBottleById(bottleId).appellation
    }

    private suspend fun updateDomain() {
        _domain.value = bottleRepository.findBottleById(bottleId).domain
    }

    private suspend fun updateCuvee() {
        _cuvee.value = bottleRepository.findBottleById(bottleId).cuvee
    }

    private suspend fun updateVintage() {
        _vintage.value = bottleRepository.findBottleById(bottleId).vintage
    }

    private suspend fun updateBottleTypeAndCapacity() {
        _bottleTypeAndCapacity.value = bottleTypeRepository.findBottleTypeByIdAndLanguage(
            bottleRepository.findBottleById(bottleId).bottleTypeId, "en_US"
        ).name+" ("+bottleTypeRepository.findBottleTypeByIdAndLanguage(
            bottleRepository.findBottleById(bottleId).bottleTypeId, "en_US"
        ).capacity+" L)"
    }

    private suspend fun updatePrice(){
        _price.value=""
        if (bottleRepository.findBottleById(bottleId).price!=null) {
            _price.value = bottleRepository.findBottleById(bottleId).price.toString() + " €"
        }
    }

    private suspend fun updateAgingCapacity(){
        _agingCapacity.value=""
        if (bottleRepository.findBottleById(bottleId).agingCapacity!=null){
            _agingCapacity.value=bottleRepository.findBottleById(bottleId).agingCapacity.toString()+" years"
        }
    }

    fun updateBottleDetailViewModel() {
        viewModelScope.launch {
            updateAppellation()
            updateDomain()
            updateCuvee()
            updateVintage()
            updateBottleTypeAndCapacity()
            updatePrice()
            updateAgingCapacity()
        }
    }
}

class BottleDetailViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BottleDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BottleDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}