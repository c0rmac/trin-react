package com.trinitcore.trinreact.ui.app.material.comp

import com.trinitcore.trinreact.ui.trinreact.TComponent
import com.trinitcore.trinreact.ui.trinreact.TProps
import com.trinitcore.trinreact.ui.trinreact.TState
import react.RBuilder
import react.RHandler

class ComponentLoad : TComponent<ComponentLoad.Props, TState>() {

    interface Props : TProps {
        var loadBlock: suspend () -> Unit
    }

    override val componentIdentifier: String
        get() = "com.trinitcore.trinreact.fundemental.app.material.comp.ComponentLoad"

    override fun RBuilder.innerRender() {
        children()
    }

}

fun RBuilder.componentLoad(loadBlock: suspend () -> Unit, handler: RHandler<ComponentLoad.Props>) = child(ComponentLoad::class) {
    attrs.loadBlock = loadBlock
    handler.invoke(this)
}