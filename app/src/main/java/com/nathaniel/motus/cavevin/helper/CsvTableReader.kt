package com.nathaniel.motus.cavevin.helper

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CsvTableReader(private val csvInputStream: InputStream) {

    fun getTable(): MutableList<List<String>> {
        val reader = BufferedReader(InputStreamReader(csvInputStream))
        val table = mutableListOf<List<String>>()

        try {
            var line = reader.readLine()
            while (line != null) {
                table.add(line.split(","))
                line=reader.readLine()
            }
        } catch (e: IOException) {
            println("IO exception")
        } finally {
            csvInputStream.close()
        }
        return table
    }
}