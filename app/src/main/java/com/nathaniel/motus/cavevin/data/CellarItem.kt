package com.nathaniel.motus.cavevin.data

data class CellarItem(
    val cellarId: Int,
    val bottleId: Int,
    val appellation: String?,
    val domain: String?,
    val cuvee: String?,
    val vintage: String?,
    val neutralWineColor: String?,
    val wineColor: String?,
    val neutralWineStillness: String?,
    val wineStillness: String?,
    val neutralBottleName: Int,
    val bottleName: String,
    val capacity: Double,
    val quantity: Int,
    val price: Double?,
    val agingCapacity: Int?,
    val comment: String?,
    val rating: Int,
    val picture: String?
)
