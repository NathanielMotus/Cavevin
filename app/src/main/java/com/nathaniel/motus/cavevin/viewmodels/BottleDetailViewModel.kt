package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.Bottle
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _appellation = MutableLiveData("")
    val appellation: LiveData<String>
        get() = _appellation

    fun onAppellationChange(appellation: String) {
        _appellation.value = appellation
    }

    private suspend fun updateAppellation() {
        _appellation.value = bottleRepository.findBottleById(bottleId).appellation
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _domain = MutableLiveData("")
    val domain: LiveData<String>
        get() = _domain

    fun onDomainChange(domain: String) {
        _domain.value = domain
    }

    private suspend fun updateDomain() {
        _domain.value = bottleRepository.findBottleById(bottleId).domain
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _cuvee = MutableLiveData("")
    val cuvee: LiveData<String>
        get() = _cuvee

    fun onCuveeChange(cuvee: String) {
        _cuvee.value = cuvee
    }

    private suspend fun updateCuvee() {
        _cuvee.value = bottleRepository.findBottleById(bottleId).cuvee
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _vintage = MutableLiveData(0)
    val vintage: LiveData<Int>
        get() = _vintage

    fun onVintageChange(vintage: Int?) {
        _vintage.value = vintage
    }

    private suspend fun updateVintage() {
        _vintage.value = bottleRepository.findBottleById(bottleId).vintage
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _bottleTypeId = MutableLiveData(1)
    val bottleTypeId: LiveData<Int>
        get() = _bottleTypeId

    private suspend fun updateBottleTypeId() {
        _bottleTypeId.value = bottleRepository.findBottleById(bottleId).bottleTypeId
    }

    fun onBottleTypeIdChange(id: Int) {
        _bottleTypeId.value = id
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _origin = MutableLiveData("")
    val origin: LiveData<String>
        get() = _origin

    fun onOriginChange(origin: String) {
        _origin.value = origin
    }

    private suspend fun updateOrigin() {
        _origin.value = ""
        if (bottleRepository.findBottleById(bottleId).origin != null) {
            _origin.value = bottleRepository.findBottleById(bottleId).origin
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _comment = MutableLiveData("")
    val comment: LiveData<String>
        get() = _comment

    fun onCommentChange(comment: String) {
        _comment.value = comment
    }

    private suspend fun updateComment() {
        _comment.value = ""
        if (bottleRepository.findBottleById(bottleId).comment != null) {
            _comment.value = bottleRepository.findBottleById(bottleId).comment
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _price: MutableLiveData<Double?> = MutableLiveData(null)
    val price: LiveData<Double?>
        get() = _price

    fun onPriceChange(price: Double?) {
        _price.value = price
    }

    private suspend fun updatePrice() {
        _price.value = bottleRepository.findBottleById(bottleId).price
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _currency = MutableLiveData("")
    val currency: LiveData<String>
        get() = _currency

    fun onCurrencyChange(currency: String?) {
        _currency.value = currency
    }

    private suspend fun updateCurrency() {
        _currency.value = bottleRepository.findBottleById(bottleId).currency
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _agingCapacity: MutableLiveData<Int?> = MutableLiveData(null)
    val agingCapacity: LiveData<Int?>
        get() = _agingCapacity

    fun onAgingCapacityChange(agingCapacity: Int?) {
        _agingCapacity.value = agingCapacity
    }

    private suspend fun updateAgingCapacity() {
        _agingCapacity.value =
            bottleRepository.findBottleById(bottleId).agingCapacity
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _wineColor :MutableLiveData<String> = MutableLiveData(WineColor.RED)
    val wineColor: LiveData<String>
        get() = _wineColor

    fun onWineColorChange(wineColor: String) {
        _wineColor.value = wineColor
    }

    private suspend fun updateWineColor() {
        _wineColor.value = bottleRepository.findBottleById(bottleId).wineColor
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _wineStillness = MutableLiveData(WineStillness.STILL)
    val wineStillness: LiveData<String>
        get() = _wineStillness

    fun onWineStillnessChange(wineStillness: String) {
        _wineStillness.value = wineStillness
    }

    private suspend fun updateWineStillness() {
        _wineStillness.value = bottleRepository.findBottleById(bottleId).wineStillness
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _imageName = MutableLiveData("")
    val imageName: LiveData<String>
        get() = _imageName

    private suspend fun updateImageName() {
        _imageName.value = ""
        if (bottleRepository.findBottleById(bottleId).picture != null) {
            _imageName.value = bottleRepository.findBottleById(bottleId).picture
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _rating = MutableLiveData(0)
    val rating: LiveData<Int>
        get() = _rating

    fun onRatingChange(rating: Int) {
        _rating.value = rating
    }

    private suspend fun updateRating() {
        _rating.value = bottleRepository.findBottleById(bottleId).rating
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _redWineTranslation = MutableLiveData("")
    val redWineTranslation: LiveData<String>
        get() = _redWineTranslation

    private suspend fun updateRedWineTranslation() {
        _redWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.RED,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _whiteWineTranslation = MutableLiveData("")
    val whiteWineTranslation: LiveData<String>
        get() = _whiteWineTranslation

    private suspend fun updateWhiteWineTranslation() {
        _whiteWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.WHITE,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _pinkWineTranslation = MutableLiveData("")
    val pinkWineTranslation: LiveData<String>
        get() = _pinkWineTranslation

    private suspend fun updatePinkWineTranslation() {
        _pinkWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.PINK,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _stillWineTranslation = MutableLiveData("")
    val stillWineTranslation: LiveData<String>
        get() = _stillWineTranslation

    private suspend fun updateStillWineTranslation() {
        _stillWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.STILL,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _sparklingWineTranslation = MutableLiveData("")
    val sparklingWineTranslation: LiveData<String>
        get() = _sparklingWineTranslation

    private suspend fun updateSparklingWineTranslation() {
        _sparklingWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.SPARKLING,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _bottleTypesAndCapacities = MutableLiveData<List<Pair<Int, String>>>()
    val bottleTypesAndCapacities: LiveData<List<Pair<Int, String>>>
        get() = _bottleTypesAndCapacities

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _appellations = MutableLiveData<List<String>>()
    val appellations: LiveData<List<String>>
        get() = _appellations

    private suspend fun updateAppellations() {
        _appellations.value = bottleRepository.getAppellations()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _domains = MutableLiveData<List<String>>()
    val domains: LiveData<List<String>>
        get() = _domains

    private suspend fun updateDomains() {
        _domains.value = bottleRepository.getDomains()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _cuvees = MutableLiveData<List<String>>()
    val cuvees: LiveData<List<String>>
        get() = _cuvees

    private suspend fun updateCuvees() {
        _cuvees.value = bottleRepository.getCuvees()
    }

    //************************************
    //update
    //************************************

    fun updateBottleDetailViewModel() {
        viewModelScope.launch {
            updateAppellation()
            updateDomain()
            updateCuvee()
            updateVintage()
            updatePrice()
            updateCurrency()
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
            updateAppellations()
            updateDomains()
            updateCuvees()
        }
    }

    suspend fun createOrUpdateBottle() {
        if (bottleId != 0)
            bottleRepository.updateBottle(
                Bottle(
                    bottleId,
                    appellation.value,
                    domain.value,
                    cuvee.value,
                    vintage.value,
                    wineColor.value!!,
                    wineStillness.value!!,
                    comment.value,
                    bottleTypeId.value!!,
                    price.value,
                    currency.value,
                    agingCapacity.value,
                    origin.value,
                    rating.value!!,
                    imageName.value
                )
            )
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