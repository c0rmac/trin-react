package com.trinitcore.trinreact.ui.trinreact

import react.RProps
import kotlin.reflect.KClass


abstract class TLocalisedComponent<P : TProps, S : TState, R : TComponentLocalisation> : TComponent<P, S> {

    constructor() : super()

    constructor(props: P) : super(props)

    abstract val localisation: KClass<R>

    enum class TSession {LANGUAGE}

    private var _text: R? = null
    val text: R
    get() {
        val a = _text ?: createLocalisationFrom(localisation)
        if (_text == null) {
            _text = a
        }
        return a
    }

    fun <T : TComponentLocalisation>createLocalisationFrom(localisation: KClass<out T>): T {
        return TLocalisationFactory.from(
            localisation,
            props?.appContext?.let { appContext -> LocalSession.getGlobalSessionAttr(appContext, TSession.LANGUAGE) }
        )
    }

    open class Style(componentIdentifier: String)
        : TComponent.Style(componentIdentifier)

}