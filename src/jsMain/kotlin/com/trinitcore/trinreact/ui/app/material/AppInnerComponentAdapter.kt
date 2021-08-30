package com.trinitcore.trinreact.ui.app.material

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

abstract class AppInnerComponentAdapter : RComponent<RProps, RState>() {

    open val hashRouter = false
    abstract val drawerItems: List<DrawerItem>
    abstract fun RBuilder.innerRender()

    override fun RBuilder.render() {
        innerRender()
    }

}