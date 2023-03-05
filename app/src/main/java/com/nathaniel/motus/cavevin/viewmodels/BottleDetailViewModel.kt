package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.data.cellar_database.WineColor
import com.nathaniel.motus.cavevin.data.cellar_database.WineStillness
import com.nathaniel.motus.cavevin.utils.systemLanguage
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

    fun onAppellationChange(appellation: String) {
        _appellation.value = appellation
    }

    private var _domain = MutableLiveData("")
    val domain: LiveData<String>
        get() = _domain

    fun onDomainChange(domain: String) {
        _domain.value = domain
    }

    private var _cuvee = MutableLiveData("")
    val cuvee: LiveData<String>
        get() = _cuvee

    fun onCuveeChange(cuvee: String) {
        _cuvee.value = cuvee
    }

    private var _vintage = MutableLiveData("")
    val vintage: LiveData<String>
        get() = _vintage

    fun onVintageChange(vintage: String) {
        _vintage.value = vintage
    }

    private var _bottleTypeId = MutableLiveData(0)
    val bottleTypeId: LiveData<Int>
        get() = _bottleTypeId

    private suspend fun updateBottleTypeId() {
        _bottleTypeId.value = bottleRepository.findBottleById(bottleId).bottleTypeId
    }

    fun onBottleTypeIdChange(id: Int) {
        _bottleTypeId.value = id
    }

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

    fun onWineColorChange(wineColor: String) {
        _wineColor.value = wineColor
    }

    private var _wineStillness = MutableLiveData("")
    val wineStillness: LiveData<String>
        get() = _wineStillness

    fun onWineStillnessChange(wineStillness: String) {
        _wineStillness.value = wineStillness
    }

    private var _imageName = MutableLiveData("")
    val imageName: LiveData<String>
        get() = _imageName

    private var _rating = MutableLiveData(0)
    val rating: LiveData<Int>
        get() = _rating

    fun onRatingChange(rating: Int) {
        _rating.value = rating
    }

    private var _redWineTranslation = MutableLiveData("")
    val redWineTranslation: LiveData<String>
        get() = _redWineTranslation

    private var _whiteWineTranslation = MutableLiveData("")
    val whiteWineTranslation: LiveData<String>
        get() = _whiteWineTranslation

    private var _pinkWineTranslation = MutableLiveData("")
    val pinkWineTranslation: LiveData<String>
        get() = _pinkWineTranslation

    private var _stillWineTranslation = MutableLiveData("")
    val stillWineTranslation: LiveData<String>
        get() = _stillWineTranslation

    private var _sparklingWineTranslation = MutableLiveData("")
    val sparklingWineTranslation: LiveData<String>
        get() = _sparklingWineTranslation

    private val _bottleTypesAndCapacities = MutableLiveData<List<Pair<Int, String>>>()
    val bottleTypesAndCapacities: LiveData<List<Pair<Int, String>>>
        get() = _bottleTypesAndCapacities

    val selectedBottleTypeItem: LiveData<Pair<Int, String>> =
        bottleTypeId.switchMap { id -> transformBottleTypeIdIntoSelectedBottleTypeItem(id) }

    private fun transformBottleTypeIdIntoSelectedBottleTypeItem(id: Int): LiveData<Pair<Int, String>> {
        var theItem = Pair(0, "")
        if (bottleTypesAndCapacities.value != null)
            for (item in bottleTypesAndCapacities.value!!)
                if (item.first == id)
                    theItem = item
        return MutableLiveData(theItem)
    }

    //************************************
    //update
    //************************************
    private suspend fun updateBottleTypesAndCapacities() {
        _bottleTypesAndCapacities.value = mutableListOf()
        for (id in bottleTypeRepository.getBottleTypeIds()) {
            (_bottleTypesAndCapacities.value as MutableList<Pair<Int, String>>).add(
                Pair(
                    id, bottleTypeRepository.findBottleTypeByIdAndLanguage(
                        id,
                        systemLanguage()
                    ).name + " " + bottleTypeRepository.findBottleTypeByIdAndLanguage(
                        id,
                        systemLanguage()
                    ).capacity + " L"
                )
            )
        }
    }


    private suspend fun updateStillWineTranslation() {
        _stillWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.STILL,
            systemLanguage()
        )
    }

    private suspend fun updateSparklingWineTranslation() {
        _sparklingWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.SPARKLING,
            systemLanguage()
        )
    }

    private suspend fun updateRedWineTranslation() {
        _redWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.RED,
            systemLanguage()
        )
    }

    private suspend fun updateWhiteWineTranslation() {
        _whiteWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.WHITE,
            systemLanguage()
        )
    }

    private suspend fun updatePinkWineTranslation() {
        _pinkWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.PINK,
            systemLanguage()
        )
    }

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
        _wineColor.value = bottleRepository.findBottleById(bottleId).wineColor
    }

    private suspend fun updateWineStillness() {
        _wineStillness.value = ""
        _wineStillness.value = bottleRepository.findBottleById(bottleId).wineStillness
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
            updatePrice()
            updateAgingCapacity()
            updateWineColor()
            updateWineStillness()
            updateOrigin()
            updateComment()
            updateImageName()
            updateRating()
            updateRedWineTranslation()
            updateWhiteWineTranslation()
            updatePinkWineTranslation()
            updateSparklingWineTranslation()
            updateStillWineTranslation()
            updateBottleTypesAndCapacities()
            updateBottleTypeId()
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