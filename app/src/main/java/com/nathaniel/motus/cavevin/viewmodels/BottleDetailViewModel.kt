package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.utils.systemLanguage
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BottleDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(application))
    private val bottleTypeRepository = BottleTypeRepository(CellarDatabase.getDatabase(application))

    //**********************************
    //State
    //**********************************
    var bottleId = 1

    private var _appellation = MutableLiveData("")
    val appellation: LiveData<String>
        get() = _appellation
    fun onAppellationChange(appellation: String) {
        _appellation.value = appellation
    }

    private var _domain = MutableLiveData("")
    val domain: LiveData<String>
        get() = _domain
    fun onDomainChange(domain:String){
        _domain.value=domain
    }

    private var _cuvee = MutableLiveData("")
    val cuvee: LiveData<String>
        get() = _cuvee
    fun onCuveeChange(cuvee:String){
        _cuvee.value=cuvee
    }

    private var _vintage = MutableLiveData("")
    val vintage: LiveData<String>
        get() = _vintage
    fun onVintageChange(vintage:String){
        _vintage.value=vintage
    }

    private var _bottleTypeAndCapacity = MutableLiveData("")
    val bottleTypeAndCapacity: LiveData<String>
        get() = _bottleTypeAndCapacity

    private var _origin = MutableLiveData("")
    val origin: LiveData<String>
        get() = _origin

    private var _comment = MutableLiveData("")
    val comment: LiveData<String>
        get() = _comment

    private var _price = MutableLiveData("")
    val price: LiveData<String>
        get() = _price

    private var _agingCapacity = MutableLiveData("")
    val agingCapacity: LiveData<String>
        get() = _agingCapacity

    private var _wineColor = MutableLiveData("")
    val wineColor: LiveData<String>
        get() = _wineColor

    private var _wineStillness = MutableLiveData("")
    val wineStillness: LiveData<String>
        get() = _wineStillness

    private var _imageName = MutableLiveData("")
    val imageName: LiveData<String>
        get() = _imageName

    private var _rating = MutableLiveData(0)
    val rating: LiveData<Int>
        get() = _rating
    fun onRatingChange(rating:Int){
        _rating.value=rating
    }

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
            bottleRepository.findBottleById(bottleId).bottleTypeId, systemLanguage()
        ).name + " (" + bottleTypeRepository.findBottleTypeByIdAndLanguage(
            bottleRepository.findBottleById(bottleId).bottleTypeId, systemLanguage()
        ).capacity + " L)"
    }

    private suspend fun updatePrice() {
        _price.value = ""
        if (bottleRepository.findBottleById(bottleId).price != null) {
            _price.value = bottleRepository.findBottleById(bottleId).price.toString() + " €"
        }
    }

    private suspend fun updateAgingCapacity() {
        _agingCapacity.value = ""
        if (bottleRepository.findBottleById(bottleId).agingCapacity != null) {
            _agingCapacity.value =
                bottleRepository.findBottleById(bottleId).agingCapacity.toString() + " years"
        }
    }

    private suspend fun updateWineColor() {
        _wineColor.value = ""
        if (bottleRepository.findBottleById(bottleId).wineColor != null) {
            _wineColor.value = bottleRepository.findBottleById(bottleId).wineColor
        }
    }

    private suspend fun updateWineStillness() {
        _wineStillness.value = ""
        if (bottleRepository.findBottleById(bottleId).wineStillness != null) {
            _wineStillness.value = bottleRepository.findBottleById(bottleId).wineStillness
        }
    }

    private suspend fun updateOrigin() {
        _origin.value = ""
        if (bottleRepository.findBottleById(bottleId).origin != null) {
            _origin.value = bottleRepository.findBottleById(bottleId).origin
        }
    }

    private suspend fun updateComment() {
        _comment.value = ""
        if (bottleRepository.findBottleById(bottleId).comment != null) {
            _comment.value = bottleRepository.findBottleById(bottleId).comment
        }
    }

    private suspend fun updateImageName() {
        _imageName.value = ""
        if (bottleRepository.findBottleById(bottleId).picture != null) {
            _imageName.value = bottleRepository.findBottleById(bottleId).picture
        }
    }

    private suspend fun updateRating() {
        _rating.value = bottleRepository.findBottleById(bottleId).rating
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
            updateWineColor()
            updateWineStillness()
            updateOrigin()
            updateComment()
            updateImageName()
            updateRating()
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