package com.trinitcore.trinreact.ui.app.material

import com.trinitcore.trinreact.ui.Context
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

abstract class AppInnerComponentAdapter(internal val providedAppContext: Context) : RComponent<AppInnerComponentAdapter.Props, RState>() {

    interface Props : RProps {
        var app: App
        var appContext: Context
    }

    val appContext: Context
    get() = props.appContext

    open val hashRouter = false
    abstract val drawerItems: List<DrawerItem>
    abstract fun RBuilder.innerRender()

    override fun RBuilder.render() {
        innerRender()
    }

}