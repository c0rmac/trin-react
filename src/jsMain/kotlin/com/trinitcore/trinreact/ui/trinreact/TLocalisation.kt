package com.trinitcore.trinreact.ui.trinreact

import com.trinitcore.trinreact.Language
import com.trinitcore.trinreact.TLocalisable
import com.trinitcore.trinreact.text
import kotlinx.browser.window
import kotlin.reflect.KClass

object TLocalisationFactory {

    fun <T : TComponentLocalisation>from(kClass: KClass<out TComponentLocalisation>): T {
        val localisedComp = kClass.js.newInstance()
        return when (language) {
            Language.EN -> localisedComp.en
            Language.GA -> localisedComp.ga
            Language.ES -> localisedComp.es ?: localisedComp.en
        } as T
    }

}

abstract class TComponentLocalisation {

    abstract val en: TComponentLocalisation
    abstract val ga: TComponentLocalisation
    open val es: TComponentLocalisation? = null

    abstract class ViewController : TComponentLocalisation() {
        open val title: String? = null
    }

}

val language: Language = when (window.navigator.language) {
    "en" -> Language.EN
    "ga" -> Language.GA
    "es" -> Language.ES
    else -> Language.EN
}

val TLocalisable.text: String get() = this.text(language)