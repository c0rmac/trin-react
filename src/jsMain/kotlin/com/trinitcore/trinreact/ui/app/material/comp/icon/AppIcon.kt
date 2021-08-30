package com.trinitcore.trinreact.ui.app.material.comp.icon

import com.trinitcore.trinreact.ui.trinreact.*
import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledImg
import kotlin.reflect.KClass

class AppIcon : TLocalisedComponent<AppIcon.Props, TState, AppIcon.Localisation>() {

    companion object {
        const val componentIdentifier = "com.trinitcore.trinreact.fundemental.app.material.comp.icon.AppIcon"
    }

    override val componentIdentifier: String
        get() = Companion.componentIdentifier

    override val localisation: KClass<Localisation>
        get() = Localisation::class

    interface Props : RProps {
        var color: Color
        var size: Size
    }

    interface State : RState {
    }

    enum class Color {
        THEME, WHITE, BLACK
    }

    enum class Size {
        LARGE, MEDIUM, SMALL
    }

    object Style : TLocalisedComponent.Style(componentIdentifier) {
        val segments by css {
            children("*") {
                float = Float.left
                marginTop = LinearDimension.auto
                marginBottom = LinearDimension.auto
                marginRight = 4.px
            }
            display = Display.flex
            width = 197.px
        }
    }

    abstract class Localisation : TComponentLocalisation() {

        object EN : Localisation() {

        }

        object GA : Localisation() {

        }

        override val en: TComponentLocalisation
            get() = EN

        override val ga: TComponentLocalisation
            get() = GA

    }

    /* Subcomponents may be added below here */
    class TextSegment : TLocalisedComponent<TextSegment.Props, TState, TextSegment.Localisation>() {

        companion object {
            val componentIdentifier = AppIcon.componentIdentifier.sub("TextSegment")
        }

        override val componentIdentifier: String
            get() = Companion.componentIdentifier

        override val localisation: KClass<Localisation>
            get() = Localisation::class

        interface Props : AppIcon.Props {

        }

        interface State : RState {

        }

        object Style : TLocalisedComponent.Style(componentIdentifier) {
            val body by css {
                fontFamily = "IconRegular"
            }
            val genetive by css {
                fontFamily = "IconRegularItalic"
                display = Display.inline
                fontSize = 22.px
                marginLeft = 1.px
                marginRight = 1.px
            }
        }

        abstract class Localisation : TComponentLocalisation() {

            object EN : Localisation() {

            }

            object GA : Localisation() {

            }

            override val en: TComponentLocalisation
                get() = EN

            override val ga: TComponentLocalisation
                get() = GA

        }

        /* Subcomponents may be added below here */

        /* Subcomponents may be added above here */

        private fun getColor() : kotlinx.css.Color {
            return when (props.color) {
                Color.BLACK -> kotlinx.css.Color.black
                Color.WHITE -> kotlinx.css.Color.white
                else -> kotlinx.css.Color.primary
            }
        }

        override fun RBuilder.innerRender() {
            styledDiv {
                css {
                    +Style.body
                    color = getColor()
                    fontSize = when (props.size) {
                        Size.LARGE -> 29.px
                        Size.MEDIUM -> 29.px
                        Size.SMALL -> 23.px
                    }
                }
                +"House"
                styledDiv {
                    css { +Style.genetive }
                    +" of "
                }
                +"Costs"
            }
        }

    }

    class IconSegment : TLocalisedComponent<IconSegment.Props, TState, IconSegment.Localisation>() {

        companion object {
            val componentIdentifier = AppIcon.componentIdentifier.sub("IconSegment")
        }

        override val componentIdentifier: String
            get() = Companion.componentIdentifier

        override val localisation: KClass<Localisation>
            get() = Localisation::class

        interface Props : AppIcon.Props {

        }

        interface State : RState {

        }

        object Style : TLocalisedComponent.Style(componentIdentifier) {
            val imageMedium by css {
                width = 40.px
                height = 40.px
            }
            val imageLarge by css {
                width = 150.px
                height = 150.px
            }
        }

        abstract class Localisation : TComponentLocalisation() {

            object EN : Localisation() {

            }

            object GA : Localisation() {

            }

            override val en: TComponentLocalisation
                get() = EN

            override val ga: TComponentLocalisation
                get() = GA

        }

        /* Subcomponents may be added below here */

        /* Subcomponents may be added above here */

        private val resourceByColor: String
        get() = when (props.color) {
            Color.BLACK -> "logo_black.png"
            Color.WHITE -> "logo_white.png"
            else -> "logo_theme.png"
        }

        override fun RBuilder.innerRender() {
            styledImg(src = com.trinitcore.trinreact.ui.trinreact.pathForResource(
                resourceByColor,
                componentIdentifier
            )
            ) {
                css {
                    when (props.size) {
                        Size.SMALL -> +Style.imageMedium
                        Size.MEDIUM -> +Style.imageMedium
                        Size.LARGE -> +Style.imageLarge
                    }
                }
            }
        }

    }
    /* Subcomponents may be added above here */

    override fun RBuilder.innerRender() {
        styledDiv {
            css { +Style.segments }
            child(IconSegment::class) { attrs.color = this@AppIcon.props.color; attrs.size = props.size }
            child(TextSegment::class) { attrs.color = this@AppIcon.props.color; attrs.size = props.size }
        }
    }

}