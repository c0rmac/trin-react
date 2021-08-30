package com.trinitcore.trinreact.ui.app.material.comp

import com.ccfraser.muirwik.components.mCircularProgress
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class DefaultLoadingCover : RComponent<RProps, RState>() {

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.flex
                width = LinearDimension("100%")
            }
            mCircularProgress {
                css {
                    margin(LinearDimension.auto)
                }
            }
        }
    }

}