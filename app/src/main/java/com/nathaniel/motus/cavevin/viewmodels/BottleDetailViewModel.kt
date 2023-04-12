package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.*
import com.nathaniel.motus.cavevin.utils.systemLanguage
import kotlinx.coroutines.launch
import java.io.File
import java.lang.IllegalArgumentException

class BottleDetailViewModel(private val currentApplication: Application) :
    AndroidViewModel(currentApplication) {

    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(currentApplication))
    private val bottleTypeRepository =
        BottleTypeRepository(CellarDatabase.getDatabase(currentApplication))
    private val wineColorRepository =
        WineColorRepository(CellarDatabase.getDatabase(currentApplication))
    private val wineStillnessRepository =
        WineStillnessRepository(CellarDatabase.getDatabase(currentApplication))
    private val bottleImageRepository = BottleImageRepository(currentApplication)
    private val stockRepository=StockRepository(CellarDatabase.getDatabase(currentApplication))

    private var bottleId=1
    private var cellarId=1

    fun updateBottleId(id: Int) {
        bottleId = id
        loadBottleDetailViewModel()
    }

    //**********************************
    //State
    //**********************************
    private var _stock=MutableLiveData(0)
    val stock:MutableLiveData<Int>
    get() = _stock

    private suspend fun loadStock(){
        _stock.value=stockRepository.getStockForBottleInCellar(bottleId,cellarId)
    }

    fun onStockChange(stock:Int?){
        _stock.value=stock
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private var _bottleImageName: MutableLiveData<String?> = MutableLiveData(null)
    val bottleImageName: MutableLiveData<String?>
        get() = _bottleImageName

    private var _bottleImageUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val bottleImageUri: LiveData<Uri?>
        get() = _bottleImageUri

    private var _bottleImageBitmap: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val bottleImageBitmap: LiveData<Bitmap?>
        get() = _bottleImageBitmap

    private suspend fun loadBottleImage() {
        _bottleImageName.value = bottleRepository.findBottleById(bottleId).picture
        _bottleImageUri.value = bottleImageRepository.getBottleImageUri(bottleImageName.value)
        _bottleImageBitmap.value = bottleImageRepository.getBottleImageBitmap(bottleImageName.value)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _appellation = MutableLiveData("")
    val appellation: LiveData<String>
        get() = _appellation

    fun onAppellationChange(appellation: String) {
        _appellation.value = appellation
    }

    private suspend fun loadAppellation() {
        _appellation.value = bottleRepository.findBottleById(bottleId).appellation
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _domain = MutableLiveData("")
    val domain: LiveData<String>
        get() = _domain

    fun onDomainChange(domain: String) {
        _domain.value = domain
    }

    private suspend fun loadDomain() {
        _domain.value = bottleRepository.findBottleById(bottleId).domain
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _cuvee = MutableLiveData("")
    val cuvee: LiveData<String>
        get() = _cuvee

    fun onCuveeChange(cuvee: String) {
        _cuvee.value = cuvee
    }

    private suspend fun loadCuvee() {
        _cuvee.value = bottleRepository.findBottleById(bottleId).cuvee
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _vintage = MutableLiveData(0)
    val vintage: LiveData<Int>
        get() = _vintage

    fun onVintageChange(vintage: Int?) {
        _vintage.value = vintage
    }

    private suspend fun loadVintage() {
        _vintage.value =bottleRepository.findBottleById(bottleId).vintage
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _bottleTypeId = MutableLiveData(1)
    val bottleTypeId: LiveData<Int>
        get() = _bottleTypeId

    private suspend fun loadBottleTypeId() {
        _bottleTypeId.value =
            bottleRepository.findBottleById(bottleId).bottleTypeId
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

    private suspend fun loadOrigin() {
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

    private suspend fun loadComment() {
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

    private suspend fun loadPrice() {
        _price.value = bottleRepository.findBottleById(bottleId).price
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _currency = MutableLiveData("")
    val currency: LiveData<String>
        get() = _currency

    fun onCurrencyChange(currency: String?) {
        _currency.value = currency
    }

    private suspend fun loadCurrency() {
        _currency.value = bottleRepository.findBottleById(bottleId).currency
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _agingCapacity: MutableLiveData<Int?> = MutableLiveData(null)
    val agingCapacity: LiveData<Int?>
        get() = _agingCapacity

    fun onAgingCapacityChange(agingCapacity: Int?) {
        _agingCapacity.value = agingCapacity
    }

    private suspend fun loadAgingCapacity() {
        _agingCapacity.value =
            bottleRepository.findBottleById(bottleId).agingCapacity
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _wineColor: MutableLiveData<String> = MutableLiveData(WineColor.RED)
    val wineColor: LiveData<String>
        get() = _wineColor

    fun onWineColorChange(wineColor: String) {
        _wineColor.value = wineColor
    }

    private suspend fun loadWineColor() {
        _wineColor.value = bottleRepository.findBottleById(bottleId).wineColor
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _wineStillness = MutableLiveData(WineStillness.STILL)
    val wineStillness: LiveData<String>
        get() = _wineStillness

    fun onWineStillnessChange(wineStillness: String) {
        _wineStillness.value = wineStillness
    }

    private suspend fun loadWineStillness() {
        _wineStillness.value =
            bottleRepository.findBottleById(bottleId).wineStillness
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _imageName = MutableLiveData("")
    val imageName: LiveData<String>
        get() = _imageName

    private suspend fun loadImageName() {
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

    private suspend fun loadRating() {
        _rating.value = bottleRepository.findBottleById(bottleId).rating
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _redWineTranslation = MutableLiveData("")
    val redWineTranslation: LiveData<String>
        get() = _redWineTranslation

    private suspend fun loadRedWineTranslation() {
        _redWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.RED,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _whiteWineTranslation = MutableLiveData("")
    val whiteWineTranslation: LiveData<String>
        get() = _whiteWineTranslation

    private suspend fun loadWhiteWineTranslation() {
        _whiteWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.WHITE,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _pinkWineTranslation = MutableLiveData("")
    val pinkWineTranslation: LiveData<String>
        get() = _pinkWineTranslation

    private suspend fun loadPinkWineTranslation() {
        _pinkWineTranslation.value = wineColorRepository.findWineColorTranslation(
            WineColor.PINK,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _stillWineTranslation = MutableLiveData("")
    val stillWineTranslation: LiveData<String>
        get() = _stillWineTranslation

    private suspend fun loadStillWineTranslation() {
        _stillWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.STILL,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var _sparklingWineTranslation = MutableLiveData("")
    val sparklingWineTranslation: LiveData<String>
        get() = _sparklingWineTranslation

    private suspend fun loadSparklingWineTranslation() {
        _sparklingWineTranslation.value = wineStillnessRepository.findWineStillnessTranslation(
            WineStillness.SPARKLING,
            systemLanguage()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _bottleTypesAndCapacities = MutableLiveData<List<Pair<Int, String>>>()
    val bottleTypesAndCapacities: LiveData<List<Pair<Int, String>>>
        get() = _bottleTypesAndCapacities

    private suspend fun loadBottleTypesAndCapacities() {
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

    private suspend fun loadAppellations() {
        _appellations.value = bottleRepository.getAppellations()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _domains = MutableLiveData<List<String>>()
    val domains: LiveData<List<String>>
        get() = _domains

    private suspend fun loadDomains() {
        _domains.value = bottleRepository.getDomains()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _cuvees = MutableLiveData<List<String>>()
    val cuvees: LiveData<List<String>>
        get() = _cuvees

    private suspend fun loadCuvees() {
        _cuvees.value = bottleRepository.getCuvees()
    }

    //************************************
    //update
    //************************************

    fun loadBottleDetailViewModel() {
        viewModelScope.launch {
            loadAppellation()
            loadDomain()
            loadCuvee()
            loadVintage()
            loadPrice()
            loadCurrency()
            loadAgingCapacity()
            loadWineColor()
            loadWineStillness()
            loadOrigin()
            loadComment()
            loadImageName()
            loadRating()
            loadRedWineTranslation()
            loadWhiteWineTranslation()
            loadPinkWineTranslation()
            loadSparklingWineTranslation()
            loadStillWineTranslation()
            loadBottleTypesAndCapacities()
            loadBottleTypeId()
            loadAppellations()
            loadDomains()
            loadCuvees()
            loadBottleImage()
            loadStock()
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //image concern
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onNewBitmapSelected(uri: Uri?) {
        //update viewModel but does not persist values to database
        if (uri != null) viewModelScope.launch {
            _bottleImageBitmap.value =
                CellarStorageUtils.getBitmapFromUri(currentApplication.applicationContext, uri)
            if (bottleImageBitmap.value != null) {
                CellarStorageUtils.saveBitmapToInternalStorage(
                    currentApplication.filesDir,
                    currentApplication.resources.getString(R.string.photo_folder_name),
                    currentApplication.resources.getString(R.string.temporary_photo_file),
                    bottleImageBitmap.value
                )
                _bottleImageUri.value =
                    bottleImageRepository.getBottleImageUri(currentApplication.resources.getString(R.string.temporary_photo_file))
            }
        }
    }

    fun onPictureTaken(isPhotoTaken: Boolean) {
        //update viewModel but does not persist values to database
        if (isPhotoTaken) viewModelScope.launch {
            _bottleImageUri.value =
                FileProvider.getUriForFile(
                    currentApplication,
                    "com.nathaniel.motus.cavevin.provider",
                    File(
                        currentApplication.cacheDir,
                        currentApplication.resources.getString(
                            R.string.temporary_photo_file
                        )
                    )
                )
            _bottleImageBitmap.value = CellarStorageUtils.getBitmapFromUri(
                currentApplication,
                bottleImageUri.value
            )
        }
    }

    fun onDeleteBottleImage() {
        _bottleImageBitmap.value = null
        _bottleImageUri.value = null
    }

    private suspend fun updateBottleDatabaseImage(): String? {
        return bottleImageRepository.replaceBottleImage(
            bottleRepository.findBottleById(bottleId).picture,
            bottleImageBitmap.value
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Appbar events
    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun onValidate() {
        viewModelScope.launch {
            bottleImageName.value = updateBottleDatabaseImage()
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
                    bottleImageName.value
                )
            )
            stockRepository.updateStock(Stock(cellarId,bottleId,stock.value?:0))
        }
    }

    init {
        loadBottleDetailViewModel()
        viewModelScope.launch {
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