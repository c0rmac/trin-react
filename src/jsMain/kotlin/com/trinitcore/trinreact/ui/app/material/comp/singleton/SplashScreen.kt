package com.clickcostz.fundemental.app.comp.singleton

import com.ccfraser.muirwik.components.transitions.mSlide
import com.trinitcore.trinreact.ui.app.material.comp.icon.AppIcon
import com.trinitcore.trinreact.ui.trinreact.*
import kotlinx.css.*
import react.RBuilder
import react.dom.br
import react.setState
import styled.css
import styled.styledDiv
import styled.styledH3
import kotlin.reflect.KClass

object SplashScreen : TLocalisedComponent<SplashScreen.Props, SplashScreen.State, SplashScreen.Localisation>() {

    override val componentIdentifier: String = "com.clickcostz.fundemental.app.comp.singleton.SplashScreen"

    override val localisation: KClass<Localisation>
        get() = Localisation::class

    interface Props : TProps {

    }

    interface State : TState {
        var visible: Boolean
    }

    override fun State.init() {
        this.visible = false
    }

    object Style : TComponent.Style(componentIdentifier) {
        val wrapper by css {
            height = 100.pct
            width = 100.pct
            position = Position.fixed
            top = 0.px
            left = 0.px
            display = Display.flex
            backgroundColor = Color.white
            zIndex = 5
        }
        val inner by css {
            margin(LinearDimension.auto)
            textAlign = TextAlign.center
        }

        val motto by css {
            fontStyle = FontStyle.italic
            marginTop = 24.px
            marginBottom = 24.px
        }

        val appIconTextContainer by css {
            position = Position.absolute
            bottom = 16.px
            textAlign = TextAlign.center
            width = 100.pct
        }
    }

    abstract class Localisation : TComponentLocalisation() {

        class EN : Localisation() {

        }

        class GA : Localisation() {

        }

        override val en: TComponentLocalisation
            get() = EN()

        override val ga: TComponentLocalisation
            get() = GA()

    }

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    fun show() = setState { this.visible = true }

    fun hide() = setState { this.visible = false }

    override fun RBuilder.innerRender() {
        mSlide(show = state.visible) {
            styledDiv {
                css { +Style.wrapper }
                styledDiv {
                    css { +Style.inner }
                    child(AppIcon.IconSegment::class) {
                        attrs.color = AppIcon.Color.THEME
                        attrs.size = AppIcon.Size.LARGE
                    }
                    styledH3 {
                        css { +Style.motto }
                        +"'Instant Home"
                        br {}
                        +"Improvement Costs'"
                    }
                }

                // TUS : OUTCASR
                styledDiv {
                    css { +Style.appIconTextContainer }
                    child(AppIcon.TextSegment::class) {
                        attrs.color = AppIcon.Color.THEME
                        attrs.size = AppIcon.Size.SMALL
                    }
                }
                // DEIREADH : OUTCAST
            }
        }
    }

}