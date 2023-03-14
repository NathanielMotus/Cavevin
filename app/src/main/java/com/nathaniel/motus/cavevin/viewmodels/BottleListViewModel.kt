package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.*
import com.nathaniel.motus.cavevin.transformers.CellarItemComparator
import com.nathaniel.motus.cavevin.utils.systemLanguage
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.function.Predicate

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

    //***********************************
    //State
    //***********************************
    private var currentCellarId = 1

    private var _redIsEnable = MutableLiveData(true)
    val redIsEnable: LiveData<Boolean>
        get() = _redIsEnable

    private var _whiteIsEnable = MutableLiveData(true)
    val whiteIsEnable: LiveData<Boolean>
        get() = _whiteIsEnable

    private var _pinkIsEnable = MutableLiveData(true)
    val pinkIsEnable: LiveData<Boolean>
        get() = _pinkIsEnable

    private var _stillIsEnable = MutableLiveData(true)
    val stillIsEnable: LiveData<Boolean>
        get() = _stillIsEnable

    private var _sparklingIsEnable = MutableLiveData(true)
    val sparklingIsEnable: LiveData<Boolean>
        get() = _sparklingIsEnable

    private var _emptyIsEnable = MutableLiveData(true)
    val emptyIsEnable: LiveData<Boolean>
        get() = _emptyIsEnable

    val cellarItems: MutableLiveData<List<CellarItem>> by lazy { MutableLiveData<List<CellarItem>>() }

    private var sortPattern = arrayListOf(
        arrayListOf(3, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10),
        arrayListOf(-1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
    )

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

    private suspend fun findBottleTypeByIdAndLanguage(id: Int, language: String) =
        bottleTypeRepository.findBottleTypeByIdAndLanguage(id, language)

    //**********************************************************************************************
    //Bottle
    //**********************************************************************************************
    fun insertBottle(bottle: Bottle) {
        viewModelScope.launch {
            bottleRepository.insertBottle(bottle)
        }
    }

    fun updateBottle(bottle: Bottle) {
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
    private suspend fun findWineColorTranslation(id: String, language: String): String =
        wineColorRepository.findWineColorTranslation(id, language)

    //************************************
    //Wine stillness
    //************************************
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
        //findBottleById(cellarEntry.bottleId).vintage,
        "2035",
        findBottleById(cellarEntry.bottleId).wineColor,
        findWineColorTranslation(findBottleById(cellarEntry.bottleId).wineColor, systemLanguage()),
        findBottleById(cellarEntry.bottleId).wineStillness,
        findWineStillnessTranslation(findBottleById(cellarEntry.bottleId).wineStillness, systemLanguage()),
        findBottleById(cellarEntry.bottleId).bottleTypeId,
        findBottleTypeByIdAndLanguage(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            systemLanguage()
        ).name,
        findBottleTypeByIdAndLanguage(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            systemLanguage()
        ).capacity,
        cellarEntry.quantity,
        findBottleById(cellarEntry.bottleId).price,
        findBottleById(cellarEntry.bottleId).agingCapacity,
        findBottleById(cellarEntry.bottleId).comment,
        findBottleById(cellarEntry.bottleId).rating,
        findBottleById(cellarEntry.bottleId).picture
    )

    private suspend fun createCellarItems(newCellarEntries: List<CellarEntry>) {
        val items: MutableList<CellarItem> = mutableListOf()
        newCellarEntries.forEach {
            items.add(createCellarItem(it))
        }
        cellarItems.value = filters(items.sortedWith(CellarItemComparator(sortPattern)))
    }

    private fun filters(cellarItems: List<CellarItem>): List<CellarItem> {
        val conditions = ArrayList<(CellarItem) -> Boolean>()
        if (!redIsEnable.value!!)
            conditions.add { it.neutralWineColor != "red" }
        if (!whiteIsEnable.value!!)
            conditions.add { it.neutralWineColor != "white" }
        if (!pinkIsEnable.value!!)
            conditions.add { it.neutralWineColor != "pink" }
        if (!stillIsEnable.value!!)
            conditions.add { it.neutralWineStillness != "still" }
        if (!sparklingIsEnable.value!!)
            conditions.add { it.neutralWineStillness != "sparkling" }
        if (!emptyIsEnable.value!!)
            conditions.add { it.quantity != 0 }

        return cellarItems.filter { candidate -> conditions.all { it(candidate) } }
    }

    fun changeFilter(filter: String) {
        when (filter) {
            STILL_FILTER -> _stillIsEnable.value = !stillIsEnable.value!!
            SPARKLING_FILTER -> _sparklingIsEnable.value = !sparklingIsEnable.value!!
            RED_FILTER -> _redIsEnable.value = !redIsEnable.value!!
            WHITE_FILTER -> _whiteIsEnable.value = !whiteIsEnable.value!!
            PINK_FILTER -> _pinkIsEnable.value = !pinkIsEnable.value!!
            EMPTY_FILTER -> _emptyIsEnable.value = !emptyIsEnable.value!!
        }
        updateBottleListViewModel()
    }


    companion object {
        const val STILL_FILTER = "stillFilter"
        const val SPARKLING_FILTER = "sparklingFilter"
        const val RED_FILTER = "redFilter"
        const val WHITE_FILTER = "whiteFilter"
        const val PINK_FILTER = "pinkFilter"
        const val EMPTY_FILTER = "emptyFilter"
    }
}

class BottleListViewModelFactory(
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