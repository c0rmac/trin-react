package com.trinitcore.trinreact.ui.trinreact

// import com.clickcostz.common.module.APP_NAME
import com.trinitcore.trinreact.ui.Context
import com.trinitcore.trinreact.ui.app.material.App
import com.trinitcore.trinreact.ui.wrapper.reactvisibilitysensor.vizSensor
import kotlinx.css.*
import react.*
import react.router.dom.RouteResultLocation
import react.router.dom.RouteResultProps
import styled.css
import styled.styledDiv
import styled.styledH3
import kotlinx.browser.window
import org.w3c.dom.url.URL

interface ViewControllerProps : TProps {
}

interface ViewControllerState : TState {

}

// Alternative Assignment 1 @ ViewController.render.vizSensor.onChange
private var currentVisibleVCInstance: ViewController<*, *, *>? = null

abstract class ViewController<P : ViewControllerProps, S : ViewControllerState, R : TComponentLocalisation.ViewController> :
    TLocalisedComponent<P, S, R> {

    override fun componentDidUpdate(prevProps: P, prevState: S, snapshot: Any) {

    }

    companion object {

        private var didCreateGlobalListener = false
        private var currentPathname: String? = null

        val visibleInstance: ViewController<*, *, *>
        get() = currentVisibleVCInstance ?: throw IllegalStateException("There currently isn't a ViewController visible.")

    }

    // TUS : MANAGED_CONTROLLER_FUNC
    // TUS : DELEGATES
    /** Managed Controller's Delegate - Pre-transition animation between ViewControllers call. */
    open fun willChangeViewController(oldPathname: String, newPathName: String) {
        if (managedController) if (!usingCustomToolbar) app.hideToolbarTitle()
    }

    /** Managed Controller's Delegate - Post transition animation between ViewControllers call. */
    open fun didChangeViewController() {
        app.setPageTitle(pageTitle)
        if (useToolbar) {
            showToolbar()
        } else {
            app.hideToolbar()
        }
    }

    /** Managed Controller's Delegate - Call for when the url parameters corresponding to this ViewController changes.
     * Return true if the parameters changed were deemed to have been changed. */
    open fun shouldChangeParameters(nextProps: P): Boolean {
        return false
    }

    /** Managed Controller's Delegate - Pre parameter change call. */
    open fun willChangeParameters(nextProps: P) {}

    /** Managed Controller's Delegate - Post parameter change call. */
    open fun didChangeParameters(nextProps: P) {}

    // TUS : Bottom Bar Delegates
    open fun shouldHideBottomBar(oldPathname: String, newPathName: String): Boolean {
        return oldPathname != newPathName
    }
    // DEIREADH : Bottom Bar Delegates
    // DEIREADH : DELEGATES

    // TUS : CONFIG
    /** Whether or not this is a Managed Controller */
    open val managedController = true
    // DEIREADH : CONFIG
    // DEIREADH : MANAGED_CONTROLLER_FUNC

    // Issue - There needs to be an alternative way of getting the path just in case. Update 14/July/2020, solved?
    lateinit var pathname: String

    /** App context */
    val appContext: Context
    get() = props.appContext ?: throw IllegalStateException("Context is not loaded.")

    /** App container */
    val app: App
    get() = props.app

    val history
    get() = (props as? RouteResultProps<*>?)?.history

    fun go(to: String) {
        history?.push(to)
    }

    override fun componentWillMount() {
        // TUS : Assign prop attributes to the ViewController instance
        // NSO 1
        currentPathname = (props as? RouteResultProps<*>?)?.location?.pathname
        pathname = (props as? RouteResultProps<*>?)?.location?.pathname ?: window.location.pathname
        // DEIREADH : Assign prop attributes to the ViewController instance

        // TUS : Create global listeners
        if (!didCreateGlobalListener) {
            (props as? RouteResultProps<*>?)?.history?.asDynamic()?.listen { location: RouteResultLocation ->
                /* Null Safety Override - Initialisation should be expected @ NSO 1 - ViewController.componentWillMount */
                val oldPathname = currentPathname!!
                val newPathName = location.pathname;
                // TUS : Link did change
                if (currentPathname != location.pathname) {
                    if (managedController) currentVisibleVCInstance?.willChangeViewController(oldPathname, newPathName)
                    if (currentVisibleVCInstance?.shouldHideBottomBar(oldPathname, newPathName) == true) {
                        if (app.isBottomBarVisible) {
                            app.hideBottomBar()
                        }
                        currentPathname = location.pathname
                    }
                }
                // DEIREADH : Link did change
            }
            didCreateGlobalListener = true
        }
        // DEIREADH : Create global listeners
    }

    override fun componentWillReceiveProps(nextProps: P) {
        if (this.managedController) {
            if (shouldChangeParameters(nextProps)) {
                this.willChangeParameters(nextProps)
                this.setState({ it }, {
                    this.didChangeParameters(nextProps)
                    window.setTimeout({
                        if (!usingCustomToolbar) app.showToolbarTitle()
                    }, 200)
                })
            }
        }
    }

    constructor() : super() {
    }

    constructor(props: P) : super(props) {
    }
    // DEIREADH : INITIALISATION

    fun getParameter(key: String): String? {
        val urlString = window.location.href
        val url = URL(urlString);
        return url.searchParams.get(key)
    }

    // TUS : Toolbar
    // TUS : DEFAULT
    fun showDefaultToolbar() {
        buildElement {
            app.useDefaultToolbar(defaultToolbarTitle, defaultNavBackPath, defaultToolbarLeftButton, defaultToolbarRightButton)
        }
    }

    open val RBuilder.defaultToolbarLeftButton: ReactElement?
        get() = null

    open val RBuilder.defaultToolbarRightButton: ReactElement?
        get() = null

    open val defaultNavBackPath: String? = null

    open val RBuilder.defaultToolbarTitle: ReactElement
        get() {
            return styledDiv {
                css {
                    marginRight = LinearDimension.auto
                    display = Display.flex
                }
                styledH3 {
                    css {
                        display = Display.inline
                        marginTop = LinearDimension.auto
                        marginBottom = LinearDimension.auto
                    }
                    title?.let { title -> +title }
                }
            }
        }
    // DEIREADH : DEFAULT

    // TUS : ADJUSTER
    private var _title: String? = null
    var title: String?
    get() = _title ?: text.title
        set(value){
            _title = value
            app.setPageTitle(pageTitle)
            //showToolbar()
        }
    // DEIREADH : ADJUSTER

    fun showToolbar() {
        if (this@ViewController.toolbar != null) {
            app.useToolbar(this@ViewController.toolbar!!)
        } else {
            showDefaultToolbar()
        }
    }
    
    open val useToolbar = true

    val usingCustomToolbar
    get() = toolbar != null

    open val toolbar: ReactElement?
    get() = null
    // DEIREADH : Toolbar

    // TUS : RENDER
    val pageTitle: String
    get() = title?.let { title -> if (pageTitleStartsWithAppName) "${appContext.appName} | $title" else "$title | ${appContext.appName}" } ?: appContext.appName

    open val pageTitleStartsWithAppName: Boolean = false

    override fun RBuilder.render() {
        vizSensor {
            this.attrs.onChange = {isVisible ->
                /* ISSUE 1 - PERFORMANCE, DIRTY_IMPLEMENTATION : This setState calls for a complete re-render of the app. This may affect performance, though perhaps only to a
                * minor extent. What is more concerning is that withOktaAuth wrapped ViewControllers cause an entirely new instance of their inner component to be created
                * when App.setState is called, causing App.setState to be called again in the inner component and consequently creating an endless loop. The temporary solution
                * to this issue is to define withOktaAuth wrapped ViewControllers as field in the App class as apposed to creating it in the route render scope. This may
                * potentially be the solution though further investigation is needed. New conventions may need to be devised.  */
                // if (isVisible && managedController && currentVisibleVCInstance != this@ViewController) {
                    // AA1
                    currentVisibleVCInstance = this@ViewController
                    didChangeViewController()
                // }
            }
            innerRender()
        }

    }

    // TUS : RENDER_CONFIG
    override var loadingIndicType: TLoadingIndicType = TLoadingIndicType.LOADING_BAR
    // DEIREADH : RENDER_CONFIG
    // DEIREADH : RENDER

}