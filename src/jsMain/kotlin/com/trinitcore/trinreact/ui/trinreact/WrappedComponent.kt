package com.trinitcore.trinreact.ui.trinreact

import kotlinx.css.RuleSet
import react.*
import styled.StyleSheet
import styled.css
import styled.styledDiv

abstract class WrappedComponent<P : TProps, S : TState, R : TComponentLocalisation> : TLocalisedComponent<P, S, R> {

    constructor() : super() {

    }

    constructor(props: P) : super(props) {

    }

    override fun RBuilder.render() {
        styledDiv {
            css { +styleSheet.wrapper }
            styledDiv {
                css { +styleSheet.inner }
                innerRender()
            }
        }
    }

    abstract class Style(componentIdentifier: String, vararg subPackages: String) :
            StyleSheet(packageForStyle(componentIdentifier, *subPackages), isStatic = true) {
        abstract val wrapper: RuleSet
        abstract val inner: RuleSet
    }

    abstract val styleSheet: Style

}