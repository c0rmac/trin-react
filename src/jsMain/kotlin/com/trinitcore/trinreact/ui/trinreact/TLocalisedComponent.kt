package com.trinitcore.trinreact.ui.trinreact

import react.RProps
import kotlin.reflect.KClass


abstract class TLocalisedComponent<P : RProps, S : TState, R : TComponentLocalisation> : TComponent<P, S> {

    constructor() : super()

    constructor(props: P) : super(props)

    abstract val localisation: KClass<R>

    val text: R
    get() = TLocalisationFactory.from(localisation)

    open class Style(componentIdentifier: String)
        : TComponent.Style(componentIdentifier)

}