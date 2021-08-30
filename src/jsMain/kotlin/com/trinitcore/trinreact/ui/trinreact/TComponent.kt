package com.trinitcore.trinreact.ui.trinreact

import com.trinitcore.trinreact.Language
import com.trinitcore.trinreact.ui.app.material.App
import com.trinitcore.trinreact.ui.app.material.comp.DefaultLoadingCover
import com.trinitcore.trinreact.exception.InternalClientRESTException
import com.trinitcore.trinreact.exception.InternalServerRESTException
import com.trinitcore.trinreact.exception.RESTException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.serialization.KSerializer
import react.*
import styled.StyleSheet


interface TProps : RProps {

}

interface TState : RState {
    var isLoading: Boolean
}

enum class TLoadingIndicType {COVER, LOADING_BAR, NONE}

abstract class TComponent<P : RProps, S : TState> : RComponent<P, S> {

    constructor() : super()

    constructor(props: P) : super(props)

    abstract val componentIdentifier: String

    open class Style(componentIdentifier: String)
        : StyleSheet(packageForStyle(componentIdentifier), isStatic = true) {

    }

    // TUS : RENDER
    abstract fun RBuilder.innerRender()

    override fun RBuilder.render() {
        if (state.isLoading && loadingIndicType == TLoadingIndicType.COVER)
            coverLoadingIndicatorRender()
        else
            innerRender()
    }

    open fun RBuilder.coverLoadingIndicatorRender() {
        child(DefaultLoadingCover::class) {

        }
    }

    // TUS : SESSION
    // TUS : For this component's session
    fun <T>setSessionAttr(key: Enum<*>, value: T?, serializer: KSerializer<T>? = null) {
        LocalSession.setSessionAttrForComp(componentIdentifier, key, value, serializer)
    }

    fun <T>getSessionAttr(key: Enum<*>, serializer: KSerializer<T>? = null): T? {
        return LocalSession.getSessionAttrForComp(componentIdentifier, key, serializer)
    }
    // DEIREADH : For this component's session

    // TUS : For the global session
    fun <T>setGlobalSessionAttr(key: Enum<*>, value: T?, serializer: KSerializer<T>? = null) {
        LocalSession.setSessionAttrForComp(App.componentIdentifier, key, value, serializer)
    }

    fun <T>getGlobalSessionAttr(key: Enum<*>, serializer: KSerializer<T>? = null): T? {
        return LocalSession.getSessionAttrForComp(App.componentIdentifier, key, serializer)
    }
    // DEIREADH : For the global session
    // DEIREADH : SESSION

    // TUS : RENDER_CONFIG
    open var loadingIndicType = TLoadingIndicType.COVER
    // DEIREADH : RENDER_CONFIG
    // DEIREADH : RENDER

    fun showErrorToast(errorMessage: String) {
        val error = when (language) {
            Language.EN -> "Something went wrong"
            Language.GA -> "Tharla rud éigin as bealach"
            Language.ES -> "Algo salió mal"
        }
        App.showToast(error, errorMessage)
    }

    // TUS : REST Handling
    open fun load(hideLoadingIndic: Boolean = false, block: suspend CoroutineScope.() -> Unit) {
        val mainScope = MainScope()

        // When this load call is made before the component has mounted
        state.isLoading = true
        // When this load call is made when the component is mounted
        setState({
            it.isLoading = true
            it
        })

        if (loadingIndicType == TLoadingIndicType.LOADING_BAR && !hideLoadingIndic)
            App.showProgressIndicator()

        mainScope.promise {
            try {
                block()
            } catch (e: Exception) {
                if (e is RESTException) {
                    if (e is InternalServerRESTException || e is InternalClientRESTException)
                        console.error(e)

                    showErrorToast(e.defaultText)
                } else {
                    // throw e
                    console.error(e)
                    showErrorToast(InternalClientRESTException("An unexpected error occurred.", language, e).defaultText)
                }
            }

            if (!hideLoadingIndic)
                setState({
                    it.isLoading = false
                    it
                }, {
                    if (loadingIndicType == TLoadingIndicType.LOADING_BAR)
                        App.hideProgressIndicator()
                })
        }
    }
    // DEIREADH : REST Handling

    fun pathForResource(resource: String) =
        pathForResource(resource, componentIdentifier)

}