package com.trinitcore.trinreact

enum class Language {EN, GA, ES}

interface TLocalisable {
    val en: String
    val ga: String
    val es: String?
}

fun TLocalisable.text(lang: Language): String {
    return when (lang) {
        Language.EN -> en
        Language.GA -> ga
        Language.ES -> es ?: en
    }
}