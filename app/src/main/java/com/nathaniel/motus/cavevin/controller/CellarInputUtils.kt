package com.nathaniel.motus.cavevin.controller

import android.content.Context
import com.nathaniel.motus.cavevin.controller.CellarInputUtils
import android.widget.Toast
import com.nathaniel.motus.cavevin.R
import java.util.*

object CellarInputUtils {
    //a utilitary class to check all inputs
    //    **********************************************************************************************
    //    Static declaration
    //    **********************************************************************************************
    private val forbiddenCharacters = ArrayList(Arrays.asList("\"", "\\", "{", "}", "[", "]"))
    private const val replacementCharacter = "*"

    //    **********************************************************************************************
    //    Input check subs
    //    **********************************************************************************************
    @JvmStatic
    fun replaceForbiddenCharacters(context: Context?, inputString: String): String {
        //replace forbidden characters by replacementCharacter
        var inputString = inputString
        val initialInputString = inputString
        for (i in forbiddenCharacters.indices) {
            inputString = inputString.replace(forbiddenCharacters[i], replacementCharacter)
        }
        if (initialInputString.compareTo(inputString) != 0) {
            Toast.makeText(
                context,
                R.string.cellar_input_utils_forbidden_characters,
                Toast.LENGTH_SHORT
            ).show()
        }
        return inputString
    }
}