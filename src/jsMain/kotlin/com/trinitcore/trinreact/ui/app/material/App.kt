package com.trinitcore.trinreact.ui.app.material

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.dialog.*
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import com.ccfraser.muirwik.components.transitions.SlideTransitionDirection
import com.ccfraser.muirwik.components.transitions.mFade
import com.ccfraser.muirwik.components.transitions.mSlide
import com.trinitcore.trinreact.ui.app.material.comp.BottomBar
import com.trinitcore.trinreact.ui.app.material.comp.Toast
import com.trinitcore.trinreact.ui.app.material.comp.singleton.OverlayBar
import com.trinitcore.trinreact.ui.app.material.comp.singleton.ProgressIndicator
import com.clickcostz.fundemental.app.comp.singleton.SplashScreen
import com.trinitcore.trinreact.ui.MAX_VP_WIDTH
import com.trinitcore.trinreact.ui.trinreact.ViewController
import com.trinitcore.trinreact.ui.trinreact.primary
import com.trinitcore.trinreact.ui.wrapper.materialicon.mArrowBackIosIcon
import com.trinitcore.trinreact.ui.wrapper.materialicon.mMenuIcon
import com.trinitcore.trinreact.ui.wrapper.reactanimateheight.animateHeight
import com.trinitcore.trinreact.ui.wrapper.reacthelmet.helmet
import kotlinx.browser.window
import kotlinx.css.*
import react.*
import react.dom.title
import react.router.dom.browserRouter
import react.router.dom.hashRouter
import styled.StyleSheet
import styled.css
import styled.styledDiv

//const val SPLASH_SCREEN_DUR = 5000
const val SPLASH_SCREEN_DUR = 1
// const val SPLASH_SCREEN_DUR = -1
const val DEFAULT_TRANS_DUR = 250
const val TOOLBAR_TRANS_DUR = DEFAULT_TRANS_DUR
const val BOTTOM_BAR_TRANS_DUR = DEFAULT_TRANS_DUR

interface AppState : RState {
    // TUS : Toolbar
    var toolbarTitle: ReactElement?
    var toolbarNavBackPath: String?
    var isToolbarTitleVisible: Boolean
    var isToolbarVisible: Boolean
    var customToolbar: ReactElement?

    // TUS : Custom Buttons
    var custLeftButton: ReactElement?
    var custoRightButton: ReactElement?
    // DEIREADH : Custom Buttons
    // DEIREADH : Toolbar

    // TUS : Global Progress Indicator
    var isProgIndcLoading: Boolean
    // DEIREADH : Global Progress Indicator

    var isBottomBarVisible: Boolean
    var bottomBarContent: ReactElement?

    var toastPropBundle: Toast.PropBundleForState?
    var isToastVisible: Boolean

    var isDimmedOverlayVisible: Boolean

    // TUS : Search Overlay
    var searchOverlayVisible: Boolean
    // DEIREADH : Search Overlay

    // TUS : Dialog
    var dialogVisible: Boolean
    var dialogTitle: String
    var dialogTextContent: String
    // DEIREADH : Dialog

    // TUS : Drawer
    var drawerVisible: Boolean
    // DEIREADH : Drawer

    // TUS : Helmet
    var pageTitle: String
    // DEIREADH : Helmet
}

interface AppProps : RProps {
    var innerComponentAdapter: AppInnerComponentAdapter
}

class App : RComponent<AppProps, AppState>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.App"
    }

    object Style : StyleSheet("com-clickcostz-webapp-Main", isStatic = true) {
        val body by css {
            height = 100.pct
            display = Display.flex
            this.put("flex-flow", "column")
        }

        val toolbar by css {
            zIndex = 2
            put("padding-left", "4px !important")
            put("padding-right", "4px !important")
            backgroundColor = Color.white
        }

        val bottomBarContainer by css {
            zIndex = 2
        }

        val toastWrapper by css {
            position = Position.fixed
            bottom = 0.px
            width = 100.pct
            zIndex = 4
        }

        val toastInner by css {
            marginLeft = LinearDimension.auto
            marginRight = LinearDimension.auto
            maxWidth = MAX_VP_WIDTH
        }

        val dimmedOverlay by css {
            backgroundColor = Color.black.withAlpha(0.4)
            position = Position.fixed
            top = 0.px
            left = 0.px
            width = 100.pct
            height = 100.pct
            zIndex = 3
        }
    }

    override fun AppState.init() {
        this.isToolbarVisible = false
        this.isToolbarTitleVisible = true
        this.searchOverlayVisible = false
        this.dialogVisible = false
        this.drawerVisible = false
        // this.pageTitle = context.appName
    }

    init {
        this.state.toastPropBundle = null
        this.state.isDimmedOverlayVisible = false

        // TUS : SplashScreen
        /*
        if (SPLASH_SCREEN_DUR != -1) {
            window.setTimeout({
                SplashScreen.hide()
            }, SPLASH_SCREEN_DUR)
        }
         */
        // DEIREADH : SplashScreen
    }

    /*
    val context: Context
    get() = props.context
     */

    // TUS : Toolbar Configuration
    // TUS : Default toolbar
    fun useDefaultToolbar(toolbarTitle: ReactElement,
                          navBackPath: String?,
                          custLeftButton: ReactElement? = null,
                          custoRightButton: ReactElement? = null
    ) {
        console.log("isToolbarTitleVisible", true)
        setState {
            this.isToolbarVisible = true
            this.isToolbarTitleVisible = true
            this.customToolbar = null
            this.toolbarTitle = toolbarTitle
            this.toolbarNavBackPath = navBackPath

            this.custLeftButton = custLeftButton
            this.custoRightButton = custoRightButton
        }
    }

    fun hideToolbarTitle() {
        console.log("isToolbarTitleVisible", false)
        setState {
            this.isToolbarTitleVisible = false
        }
    }

    fun showToolbarTitle() {
        console.log("isToolbarTitleVisible", true)
        setState {
            this.isToolbarTitleVisible = true
        }
    }
    // DEIREADH : Default toolbar

    // TUS : Custom toolbar
    fun useToolbar(toolbar: ReactElement) {
        setState({
            it.customToolbar = toolbar
            it
        }, {
            setState({
                it.isToolbarVisible = true
                it
            })
        })
    }
    // DEIREADH : Custom toolbar
    fun hideToolbar() {
        println("Hide toolbar")
        setState {
            this.isToolbarVisible = false
        }
    }
    // DEIREADH : Toolbar Configuration

    // TUS : Toast Configuration
    fun showToast(titleText: String, textContent: String, titleIcon: RComponent<MIconProps,RState>? = null) {
        showToast(titleText, buildElement {
            +textContent
        }, titleIcon)
    }

    fun showToast(titleText: String, textContent: ReactElement, titleIcon: RComponent<MIconProps,RState>? = null) {
        showToast(titleText, titleIcon, buildElement {
            styledDiv {
                css {
                    paddingLeft = 8.px
                    paddingRight = 8.px
                    paddingBottom = 16.px
                }
                +textContent
            }
        })
    }

    fun showToast(titleText: String, titleIcon: RComponent<MIconProps,RState>?, content: ReactElement) {
        setState {
            toastPropBundle = Toast.PropBundleForState(titleText, titleIcon, content)
            isDimmedOverlayVisible = true
            isToastVisible = true
        }
    }

    fun dismissToast() {
        setState {
            isToastVisible = false
            isDimmedOverlayVisible = false
        }
    }
    // DEIREADH : Toast Configuration

    // TUS : Dialog Configuration
    fun showDialog(title: String, textContent: String) {
        setState {
            this.dialogTitle = title
            this.dialogTextContent = textContent
            this.dialogVisible = true
        }
    }

    fun hideDialog() {
        setState { this.dialogVisible = false }
    }
    // DEIREADH : Dialog Configuration

    // TUS : Bottom Bar Configuration
    /** bottomBarContentDispatchQue */
    private var bottomBarContentDispQue = mutableListOf<ReactElement?>()
    private var dispQueFrozen = false

    /** executeBottomBarTransaction */
    private fun executeBottBarTransaction(isVisible: Boolean, content: ReactElement?) {
        setState(
            {
                if (content == null) it.isBottomBarVisible = false
                else it.isBottomBarVisible = !isVisible
                it
            },
            {
                dispQueFrozen = true
                window.setTimeout({
                    dispQueFrozen = false
                    setState({
                        it.bottomBarContent = content
                        it.isBottomBarVisible = isVisible
                        it
                    }, {
                        bottomBarContentDispQue.remove(content)
                        bottomBarContentDispQue.firstOrNull()?.let {
                            executeBottBarTransaction(!isVisible, it)
                        }
                    })
                }, BOTTOM_BAR_TRANS_DUR)
            }
        )
    }

    fun showBottomBar(content: ReactElement?) {
        bottomBarContentDispQue.add(content)

        if (bottomBarContentDispQue.size == 1 && !dispQueFrozen) {
            executeBottBarTransaction(content != null, content)
        }
    }

    fun hideBottomBar() {
        showBottomBar(null)
    }

    val isBottomBarVisible
        get() = state.isBottomBarVisible
    // DEIREADH : Bottom Bar Configuration

    // TUS : Global Progress Indicator
    fun showProgressIndicator() {
        ProgressIndicator.setLoading(true)
    }

    fun hideProgressIndicator() {
        ProgressIndicator.setLoading(false)
    }
    // DEIREADH : Global Progress Indicator

    // TUS : Search Overlay
    fun showSearchOverlay() {
        setState { this.searchOverlayVisible = true }
    }

    fun hideSearchOverlay() {
        setState { this.searchOverlayVisible = false }
    }
    // DEIREADH : Search Overlay

    // TUS : DRAWER

    fun showDrawer() = setState { this.drawerVisible = true }

    fun hideDrawer() = setState { this.drawerVisible = false }
    // DEIREADH : DRAWER

    // TUS : Helmet
    fun setPageTitle(title: String) = setState { this.pageTitle = title }
    // DEIREADH : Helmet

    override fun RBuilder.render() {
        @Suppress("UnsafeCastFromDynamic")
        val themeOptions: ThemeOptions =
            js("({palette: { primary: { main: '#039BE5', light: '#B3E5FC' } }, typography : { fontFamily: ['Fira Sans Condensed'] } })")

        console.log("Render call")

        fun renderRouter(handler: RHandler<RProps>) {
            if (props.innerComponentAdapter.hashRouter) {
                hashRouter(handler = handler)
            } else {
                browserRouter(handler = handler)
            }
        }

        renderRouter {
            mThemeProvider(createMuiTheme(themeOptions)) {
                styledDiv {
                    css { +Style.body }
                    helmet {
                        title { +state.pageTitle }
                    }

                    animateHeight(if (state.isToolbarVisible) "auto" else "0", TOOLBAR_TRANS_DUR) {
                        if (state.customToolbar != null) {
                            +state.customToolbar!!
                        } else {
                            // TUS : Default toolbar impl.
                            mAppBar(position = MAppBarPosition.relative) {
                                css {
                                    put("z-index", "2 !important")
                                    put("box-shadow", "0px 0px 2px -1px rgba(0,0,0,0.2), 0px 0px 0px 0px rgba(0,0,0,0.14), 0px 1px 10px 0px rgba(0,0,0,0.12) !important")
                                }
                                mToolbar(variant = ToolbarVariant.dense) {
                                    css { +Style.toolbar }
                                    mFade(show = state.isToolbarTitleVisible) {
                                        styledDiv {
                                            css {
                                                display = Display.flex
                                                width = 100.pct
                                            }
                                            +(state.custLeftButton ?: mIconButton(
                                                addAsChild = false,
                                                onClick = {
                                                    state.toolbarNavBackPath?.let { toolbarNavBackPath ->
                                                        // Issue - DIRTY_IMPLEMENTATION
                                                        ViewController.visibleInstance?.history?.push(toolbarNavBackPath)
                                                    } ?: window.history.go(-1)
                                                }) {
                                                mArrowBackIosIcon {
                                                    css { color = Color.primary }
                                                }
                                            })

                                            state.toolbarTitle?.let {
                                                +it
                                            }

                                            +(state.custoRightButton ?: mIconButton(addAsChild = false, onClick = { showDrawer() }) {
                                                mMenuIcon {
                                                    css { color = Color.primary }
                                                }
                                            })
                                        }
                                    }
                                }
                            }
                            // DEIREADH : Default toolbar impl.
                        }
                    }

                    // TUS : SplashScreen
                    child(SplashScreen::class) {

                    }
                    // DEIREADH : SplashScreen

                    // TUS : Dialog
                    mDialog(open = state.dialogVisible, onClose = { _, _ -> hideDialog() }) {
                        mDialogTitle(text = state.dialogTitle)
                        mDialogContent {
                            mDialogContentText(text = state.dialogTextContent)
                        }
                        mDialogActions {
                            mButton("Dismiss", onClick = { hideDialog() }, color = MColor.primary)
                        }
                    }
                    // DEIREADH : Dialog

                    // TUS : Global Progress indicator
                    styledDiv {
                        child(ProgressIndicator::class) { }
                    }
                    // DEIREADH : Global Progress indicator

                    // TUS : Overlay Bar
                    child(OverlayBar::class) { }
                    // DEIREADH : Overlay Bar

                    /*
                    security("https://dev-224067.okta.com/oauth2/default",
                        "0oad23juxrzzIBa2d4x6",
                        "http://localhost:8080/implicit/callback",
                        true) {
                     */
                    val innerComponentAdapter = child(props.innerComponentAdapter::class) {
                        attrs.app = this@App
                        attrs.appContext = props.innerComponentAdapter.providedAppContext
                    }
                    // }

                    mDrawer(open = state.drawerVisible, onClose = { hideDrawer() }) {
                        mList {
                            props.innerComponentAdapter?.drawerItems?.forEach { drawerItem ->
                                mListItem(
                                    primaryText = drawerItem.title,
                                    onClick = {
                                        if (drawerItem.link != null) ViewController.visibleInstance.go(to = drawerItem.link);
                                        drawerItem.action?.invoke()
                                        hideDrawer()
                                    }
                                )
                            }
                        }
                    }

                    animateHeight(if (state.isBottomBarVisible) "auto" else "0", BOTTOM_BAR_TRANS_DUR) {
                        styledDiv {
                            css { +Style.bottomBarContainer }
                            child(BottomBar::class) {
                                if (state.bottomBarContent != null) +state.bottomBarContent!!
                            }
                        }
                    }


                    mFade(show = state.isDimmedOverlayVisible) {
                        styledDiv {
                            css { +Style.dimmedOverlay }
                        }
                    }

                    val toastPropBundle = state.toastPropBundle
                    if (toastPropBundle != null)
                        mSlide(show = state.isToastVisible, direction = SlideTransitionDirection.up) {
                            styledDiv {
                                css { +Style.toastWrapper }
                                styledDiv {
                                    css { +Style.toastInner }
                                    child(Toast::class) { attrs {
                                        this.propBundleForState = toastPropBundle
                                        this.closeButtonOnClick = {
                                            dismissToast()
                                        }
                                    } }
                                }
                            }
                        }
                }
            }
        }
    }

}