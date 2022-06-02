package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BottleListViewModel(
    application: Application
) : AndroidViewModel(application) {

    //todo : implement a view model for each view, with needed functions only
    //todo : main view, bottle detail view, bottle type management view, cellar transfer view, filter edition view
    //todo : implement transferBottle(bottleId, fromCellar, toCellar, quantity)
    //todo : implement mergeCellar(cellar, inCellar)

    fun updateBottleListViewModel() {
        viewModelScope.launch {
            cellarRepository.getCellarEntries().collect {
                createCellarItems(it)
            }
        }
    }

    private val cellarRepository = CellarRepository(CellarDatabase.getDatabase(application))
    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(application))
    private val bottleTypeRepository = BottleTypeRepository(CellarDatabase.getDatabase(application))
    private val stockRepository = StockRepository(CellarDatabase.getDatabase(application))
    private val wineColorRepository = WineColorRepository(CellarDatabase.getDatabase(application))
    private val wineStillnessRepository =
        WineStillnessRepository(CellarDatabase.getDatabase(application))

    val cellars = cellarRepository.getCellars()
    val bottleTypes: LiveData<List<BottleType>> = bottleTypeRepository.getBottleTypes()
    val stocks: LiveData<List<Stock>> = stockRepository.getStocks()

    //***********************************
    //State
    //***********************************
    private var currentCellarId = 1
    private var language = "en_US"

    val cellarItems: MutableLiveData<List<CellarItem>> by lazy { MutableLiveData<List<CellarItem>>() }

    //**********************************************************************************************
    //Cellar
    //**********************************************************************************************

    fun insertCellar(cellar: Cellar) {
        viewModelScope.launch {
            cellarRepository.insertCellar(cellar)
        }
    }

    fun deleteCellar(cellar: Cellar) {
        viewModelScope.launch {
            cellarRepository.deleteCellar(cellar)
        }
    }

    //**********************************************************************************************
    //Bottle Type
    //**********************************************************************************************
    fun insertBottleType(bottleType: BottleType) {
        viewModelScope.launch {
            bottleTypeRepository.insertBottleType(bottleType)
        }
    }

    suspend fun getBottleTypeIds() = bottleTypeRepository.getBottleTypeIds()

    suspend fun findBottleTypeByIdAndLanguage(id: Int, language: String) =
        bottleTypeRepository.findBottleTypeByIdAndLanguage(id, language)

    //**********************************************************************************************
    //Bottle
    //**********************************************************************************************
    fun insertBottle(bottle: Bottle) {
        viewModelScope.launch {
            bottleRepository.insertBottle(bottle)
        }
    }

    fun updateBottle(bottle:Bottle){
        viewModelScope.launch {
            bottleRepository.updateBottle(bottle)
        }
    }

    private suspend fun findBottleById(id: Int): Bottle =
        bottleRepository.findBottleById(id)

    //**********************************************************************************************
    //Stock
    //**********************************************************************************************
    fun updateStock(stock: Stock) {
        viewModelScope.launch {
            stockRepository.updateStock(stock)
        }
    }

    //*************************************
    //Wine color
    //*************************************
    fun insertWineColor(wineColor: WineColor) =
        viewModelScope.launch { wineColorRepository.insertWineColor(wineColor) }

    private suspend fun getWineColorLanguages(id: String): List<String> =
        wineColorRepository.getWineColorLanguages(id)

    private suspend fun findWineColorTranslation(id: String, language: String): String =
        wineColorRepository.findWineColorTranslation(id, language)

    //************************************
    //Wine stillness
    //************************************
    fun insertWineStillness(wineStillness: WineStillness) =
        viewModelScope.launch { wineStillnessRepository.insertWineStillness(wineStillness) }

    private suspend fun getWineStillnessLanguages(id: String?): List<String>? =
        if (id != null)
            wineStillnessRepository.getWineStillnessLanguages(id)
        else null

    private suspend fun findWineStillnessTranslation(id: String?, language: String): String? =
        if (id != null)
            wineStillnessRepository.findWineStillnessTranslation(id, language)
        else null

    //******************************
    //CellarItem
    //******************************
    private suspend fun createCellarItem(cellarEntry: CellarEntry) = CellarItem(
        cellarEntry.cellarId,
        cellarEntry.bottleId,
        findBottleById(cellarEntry.bottleId).appellation,
        findBottleById(cellarEntry.bottleId).domain,
        findBottleById(cellarEntry.bottleId).cuvee,
        findBottleById(cellarEntry.bottleId).vintage,
        findWineColorTranslation(findBottleById(cellarEntry.bottleId).wineColor, language),
        findWineStillnessTranslation(findBottleById(cellarEntry.bottleId).wineStillness, language),
        findBottleTypeByIdAndLanguage(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            language
        ).name,
        findBottleTypeByIdAndLanguage(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            language
        ).capacity,
        cellarEntry.quantity,
        findBottleById(cellarEntry.bottleId).price,
        findBottleById(cellarEntry.bottleId).agingCapacity,
        findBottleById(cellarEntry.bottleId).comment,
        findBottleById(cellarEntry.bottleId).rating,
        findBottleById(cellarEntry.bottleId).picture
    )

    private suspend fun createCellarItems(newCellarEntries: List<CellarEntry>) {
        var items: MutableList<CellarItem> = mutableListOf()
        newCellarEntries.forEach {
            items.add(createCellarItem(it))
        }
        cellarItems.value = items
    }

}

class CellarViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BottleListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BottleListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}