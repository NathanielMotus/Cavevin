package com.nathaniel.motus.cavevin.data.cellar_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathaniel.motus.cavevin.initialization.CellarDatabaseInitializer
import com.nathaniel.motus.cavevin.upgrade.UpGraderToVersionCode5
import com.nathaniel.motus.cavevin.utils.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Cellar::class, Stock::class, Bottle::class, BottleType::class, WineColor::class, WineStillness::class],
    version = 1, exportSchema = false
)
abstract class CellarDatabase : RoomDatabase() {

    abstract fun cellarDao(): CellarDao
    abstract fun stockDao(): StockDao
    abstract fun bottleDao(): BottleDao
    abstract fun bottleTypeDao(): BottleTypeDao
    abstract fun wineColorDao(): WineColorDao
    abstract fun wineStillnessDao(): WineStillnessDao

    companion object {
        @Volatile
        private var INSTANCE: CellarDatabase? = null

        fun getDatabase(context: Context): CellarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CellarDatabase::class.java, DATABASE_NAME
                )
                    .addCallback(CellarDatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class CellarDatabaseCallback(val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    with(CellarDatabaseInitializer(context)) {
                        this.populateWineStillnessTable()
                        this.populateWineColorTable()
                        this.populateBottleTypeTable()
                    }
                    UpGraderToVersionCode5(context).execute()
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                //todo : if version code is new add or replace new languages
            }
        }

    }
}