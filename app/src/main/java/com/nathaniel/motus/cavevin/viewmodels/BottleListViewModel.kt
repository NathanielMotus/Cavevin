package com.nathaniel.motus.cavevin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nathaniel.motus.cavevin.data.*
import com.nathaniel.motus.cavevin.data.cellar_database.*
import com.nathaniel.motus.cavevin.utils.systemLanguage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BottleListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    //todo : main view, bottle detail view, bottle type management view, cellar transfer view, filter edition view
    //todo : implement transferBottle(bottleId, fromCellar, toCellar, quantity)
    //todo : implement mergeCellar(cellar, inCellar)


    private val cellarRepository = CellarRepository(CellarDatabase.getDatabase(application))
    private val bottleRepository = BottleRepository(CellarDatabase.getDatabase(application))
    private val bottleTypeRepository = BottleTypeRepository(CellarDatabase.getDatabase(application))
    private val stockRepository = StockRepository(CellarDatabase.getDatabase(application))
    private val wineColorRepository = WineColorRepository(CellarDatabase.getDatabase(application))
    private val wineStillnessRepository =
        WineStillnessRepository(CellarDatabase.getDatabase(application))
    private val bottleImageRepository = BottleImageRepository(application = application)

    private fun loadBottleListViewModel() {
        viewModelScope.launch {
            loadCellarName()
            loadCellars()
            cellarRepository.getCellarEntries().collect {
                createCellarItems(it)
            }
        }
    }

    //***********************************
    //State
    //***********************************
    private var _currentCellarId = MutableLiveData(1)
    val currentCellarId: MutableLiveData<Int>
        get() = _currentCellarId

    private var _cellarName = MutableLiveData("")
    val cellarName: LiveData<String>
        get() = _cellarName

    private suspend fun loadCellarName() {
        _cellarName.value = currentCellarId.value?.let { cellarRepository.getCellar(it).name }
    }

    fun onCellarIdChange(id: Int) {
        if (id != 0) {
            viewModelScope.launch {
                _currentCellarId.value = id
                loadBottleListViewModel()
            }
        }
    }

    private val _cellars = MutableLiveData<List<Cellar>>()
    val cellars: LiveData<List<Cellar>>
        get() = _cellars

    private suspend fun loadCellars() {
        _cellars.value = cellarRepository.getCellars()
    }

    private val _cellarsForSpinner: MutableLiveData<List<Pair<Int, String>>> =
        cellars.switchMap { transformCellarsIntoCellarsForSpinner(it) } as MutableLiveData<List<Pair<Int, String>>>
    val cellarsForSpinner: LiveData<List<Pair<Int, String>>>
        get() = _cellarsForSpinner

    private val _selectedCellarForSpinner = MutableLiveData<Pair<Int, String>>()
    val selectedCellarForSpinner: LiveData<Pair<Int, String>>
        get() = _selectedCellarForSpinner

    private fun transformCellarsIntoCellarsForSpinner(cellars: List<Cellar>): MutableLiveData<List<Pair<Int, String>>> {
        val cellarsForSpinner: MutableList<Pair<Int, String>> = mutableListOf()
        cellars.forEach {
            cellarsForSpinner.add(Pair(it.id, it.name))
            if (it.id == currentCellarId.value) _selectedCellarForSpinner.value =
                Pair(it.id, it.name)
        }
        return MutableLiveData(cellarsForSpinner)
    }
    //**********************************************************************************************

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

    fun insertAndOpenCellar(cellar: Cellar) {
        viewModelScope.launch {
            cellarRepository.insertCellar(cellar)
            getLastCellarId()?.let { onCellarIdChange(it) }
            loadBottleListViewModel()
        }
    }

    fun deleteCellar(cellar: Cellar) {
        viewModelScope.launch {
            cellarRepository.deleteCellar(cellar)
        }
    }

    suspend fun getLastCellarId() = cellarRepository.getLastCellarId()

    fun updateCellar(cellar: Cellar) {
        viewModelScope.launch {
            cellarRepository.updateCellar(cellar)
            loadBottleListViewModel()
        }
    }

    suspend fun deleteCellarStocks(cellarId: Int) {
        stockRepository.deleteCellarStocks(cellarId)
    }

    suspend fun deleteCellar(cellarId: Int) {
        cellarRepository.deleteCellar(cellarId)
    }

    fun clearAndDeleteCellar(cellarId:Int){
        viewModelScope.launch {
            deleteCellarStocks(cellarId)
            deleteCellar(cellarId)
            _currentCellarId.value=getLastCellarId()
            loadBottleListViewModel()
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
    private fun updateStock(stock: Stock) {
        viewModelScope.launch {
            stockRepository.updateStock(stock)
        }
    }

    fun updateStockForBottleInCurrentCellar(bottleId: Int, stock: Int) {
        updateStock(Stock(currentCellarId.value!!, bottleId, stock))
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
        findBottleById(cellarEntry.bottleId).vintage,
        findBottleById(cellarEntry.bottleId).wineColor,
        findBottleById(cellarEntry.bottleId).wineStillness,
        bottleTypeRepository.findBottleTypeByIdAndLanguage(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            systemLanguage()
        ).capacity,
        Pair(
            findBottleById(cellarEntry.bottleId).bottleTypeId,
            bottleTypeRepository.findBottleTypeByIdAndLanguage(
                findBottleById(cellarEntry.bottleId).bottleTypeId,
                systemLanguage()
            ).let { "${it.name} ${it.capacity} L" }
        ),
        cellarEntry.quantity,
        findBottleById(cellarEntry.bottleId).price,
        findBottleById(cellarEntry.bottleId).agingCapacity,
        findBottleById(cellarEntry.bottleId).comment,
        findBottleById(cellarEntry.bottleId).rating,
        if (findBottleById(cellarEntry.bottleId).picture != null
            && findBottleById(cellarEntry.bottleId).picture != ""
        ) bottleImageRepository.getBottleBitmapThumbnail(
            findBottleById(cellarEntry.bottleId).picture
        ) else null,
        if (findBottleById(cellarEntry.bottleId).picture != null
            && findBottleById(cellarEntry.bottleId).picture != ""
        ) bottleImageRepository.getBottleImageUri(
            findBottleById(cellarEntry.bottleId).picture
        ) else null
    )

    private suspend fun createCellarItems(newCellarEntries: List<CellarEntry>) {
        val items: MutableList<CellarItem> = mutableListOf()
        newCellarEntries.forEach {
            if (it.cellarId == currentCellarId.value)
                items.add(createCellarItem(it))
        }
        //cellarItems.value = filters(items.sortedWith(CellarItemComparator(sortPattern)))
        cellarItems.value = items
    }

    private fun filters(cellarItems: List<CellarItem>): List<CellarItem> {
        val conditions = ArrayList<(CellarItem) -> Boolean>()
        if (!redIsEnable.value!!)
            conditions.add { it.wineColor != "red" }
        if (!whiteIsEnable.value!!)
            conditions.add { it.wineColor != "white" }
        if (!pinkIsEnable.value!!)
            conditions.add { it.wineColor != "pink" }
        if (!stillIsEnable.value!!)
            conditions.add { it.wineStillness != "still" }
        if (!sparklingIsEnable.value!!)
            conditions.add { it.wineStillness != "sparkling" }
        if (!emptyIsEnable.value!!)
            conditions.add { it.stock != 0 }

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
        loadBottleListViewModel()
    }


    companion object {
        const val STILL_FILTER = "stillFilter"
        const val SPARKLING_FILTER = "sparklingFilter"
        const val RED_FILTER = "redFilter"
        const val WHITE_FILTER = "whiteFilter"
        const val PINK_FILTER = "pinkFilter"
        const val EMPTY_FILTER = "emptyFilter"
    }

    init {
        loadBottleListViewModel()
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