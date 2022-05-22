package com.nathaniel.motus.cavevin.utils

fun rootLanguage(language: String) = language.split("_")[0]

fun findBaseLanguage(language: String, inLanguages: List<String>): String {
    //by default baseLanguage is BASE_LANGUAGE
    var baseLanguage = BASE_LANGUAGE
    //if BASE_LANGUAGE not available, baseLanguage=first available language
    if (inLanguages.indexOf(BASE_LANGUAGE) == -1) baseLanguage = inLanguages[0]
    //if there is a language with same root, take this one
    inLanguages.forEach {
        if (rootLanguage(it) == rootLanguage(language))
            baseLanguage = it
    }
    //if language is available, take language
    if (inLanguages.indexOf(language) != -1)
        baseLanguage = language

    return baseLanguage
}