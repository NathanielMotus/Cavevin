package com.nathaniel.motus.cavevin.model

import com.nathaniel.motus.cavevin.model.Bottle
import com.nathaniel.motus.cavevin.model.Cellar
import com.nathaniel.motus.cavevin.model.JsonObject
import com.nathaniel.motus.cavevin.model.CellComparator
import java.util.ArrayList

class JsonObject//        create an empty JsonObject     //    **********************************************************************************************
//    constructors
//    **********************************************************************************************
    : Any() {
    //    **********************************************************************************************
    //    class fields
    //    **********************************************************************************************
    private var mJsonString = "{}"

    //    **********************************************************************************************
    //    Converters
    //    **********************************************************************************************
    fun jsonToString(): String {
        //return a String
        return mJsonString
    }

    //    **********************************************************************************************
    //    Writers
    //    **********************************************************************************************
    fun addJsonObject(jsonObject: JsonObject) {
        //append a JsonObject to this
        var root = mJsonString.substring(0, mJsonString.length - 1)
        val rootLastChar = root.substring(root.length - 1)
        if (rootLastChar.compareTo("{") != 0 && rootLastChar.compareTo("[") != 0) root = "$root,"
        mJsonString = root + jsonObject.mJsonString + "}"
    }

    fun addKeyValue(key: String, value: String) {
        //append a key/value pair to this JsonObject
        //value is a String
        var root = mJsonString.substring(0, mJsonString.length - 1)
        if (root.length > 1) root = "$root,"
        mJsonString = "$root\"$key\":\"$value\"}"
    }

    fun addKeyValue(key: String, objectArrayList: ArrayList<Any>) {
        //append a key/value pair to this JsonObject
        //value can be :
        //      ArrayList<Integer>
        //      ArrayList<JsonObject>
        //      (to be continued if need be)
        var root = mJsonString.substring(0, mJsonString.length - 1)
        if (root.length > 1) root = "$root,"
        mJsonString = "$root\"$key\":["
        for (i in objectArrayList.indices) {
            if (i != 0) mJsonString = "$mJsonString,"
            val objectClassString = objectArrayList[i].javaClass.toString()
            mJsonString = when (objectClassString) {
                "class java.lang.Integer" -> mJsonString + "\"" + objectArrayList[i] + "\""
                "class com.nathaniel.motus.cavevin.model.JsonObject" -> {
                    val jsonObject = objectArrayList[i] as JsonObject
                    mJsonString + jsonObject.mJsonString
                }
                else -> mJsonString + objectClassString
            }
        }
        mJsonString = "$mJsonString]}"
    }

    //    **********************************************************************************************
    //    Readers
    //    **********************************************************************************************
    fun getKeyValue(key: String): String {
        //return the value of the first occurrence of key in this JsonObject
        val keyIndex = mJsonString.indexOf(key)
        return if (keyIndex != -1) {
            val startValueIndex = keyIndex + key.length + 3
            if (mJsonString.substring(startValueIndex - 1, startValueIndex).compareTo("\"") == 0) {
                val endValueIndex = mJsonString.indexOf("\"", startValueIndex)
                mJsonString.substring(startValueIndex, endValueIndex)
            } else {
                "NOT A VALUE ! : " + mJsonString.substring(
                    startValueIndex - 1,
                    startValueIndex
                )
            }
        } else ""
    }

    fun getKeyValueArray(key: String): ArrayList<Any> {
        //return an ArrayList<Object> from a key
        //Object can be
        //      Integer
        //      JsonObject
        //      (to be continued if need be)
        val objectArrayList = ArrayList<Any>()
        val keyIndex = mJsonString.indexOf(key)
        if (keyIndex != -1) {
            val startValueIndex = keyIndex + key.length + 3
            var currentString: String
            if (mJsonString.substring(startValueIndex - 1, startValueIndex).compareTo("[") == 0) {
                val endValueIndex = getClosureIndex(startValueIndex - 1) - 1
                var i = startValueIndex
                while (i < endValueIndex) {
                    currentString = mJsonString.substring(i, getClosureIndex(i) + 1)
                    if (currentString.substring(0, 1).compareTo("{") == 0) objectArrayList.add(
                        stringToJsonObject(currentString)
                    ) else objectArrayList.add(
                        currentString.substring(1, currentString.length - 1).toInt()
                    )
                    i = i + currentString.length + 1
                }
            } else objectArrayList.add(
                "NOT AN ARRAY ! : " + mJsonString.substring(
                    startValueIndex - 1,
                    startValueIndex
                )
            )
        }
        return objectArrayList
    }

    private fun getClosureIndex(openIndex: Int): Int {
        //return the closure index of the JsonObject or Array
        //parse the string in search of closure character ("]" or "}")
        //ignoring those between 2 quotation marks
        val openChar = mJsonString.substring(openIndex, openIndex + 1)
        val closeChar: String
        closeChar = when (openChar) {
            "[" -> "]"
            "{" -> "}"
            "\"" -> "\""
            else -> return -1
        }
        var nbOpenChar = 1 //+1 when openChar met, -1 when closeChar met
        var nbQuote = 0
        var i = openIndex + 1
        while (i < mJsonString.length && nbOpenChar > 0) {
            if (mJsonString.substring(i, i + 1).compareTo("\"") == 0) nbQuote = (nbQuote + 1) % 2
            if (mJsonString.substring(i, i + 1)
                    .compareTo(openChar) == 0 && nbQuote == 0
            ) nbOpenChar++
            if (mJsonString.substring(i, i + 1)
                    .compareTo(closeChar) == 0 && nbQuote == 0
            ) nbOpenChar--

            //special case for "\""
            if (mJsonString.substring(i, i + 1).compareTo(closeChar) == 0 && closeChar.compareTo(
                    openChar
                ) == 0
            ) nbOpenChar = 0
            i++
        }
        return if (nbOpenChar == 0) i - 1 else -1
    }

    companion object {
        fun stringToJsonObject(string: String): JsonObject {
            //return a JsonObject which mJsonString is string
            val jsonObject = JsonObject()
            jsonObject.mJsonString = string
            return jsonObject
        }
    }
}