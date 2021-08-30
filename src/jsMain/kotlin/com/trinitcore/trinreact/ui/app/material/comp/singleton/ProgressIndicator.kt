package com.trinitcore.trinreact.ui.app.material.comp.singleton

import com.ccfraser.muirwik.components.mLinearProgress
import com.ccfraser.muirwik.components.transitions.mFade
import kotlinx.css.marginBottom
import kotlinx.css.px
import react.*
import styled.css
import styled.styledDiv

object ProgressIndicator : RComponent<RProps, ProgressIndicator.State>() {

    interface State : RState {
        var isProgIndcLoading: Boolean
    }

    fun setLoading(loading: Boolean) {
        setState {
            this.isProgIndcLoading = loading
        }
    }

    override fun RBuilder.render() {
        mFade(show = state.isProgIndcLoading) {
            styledDiv {
                css {
                    /* Counteract the height the progress bar takes up occupying the region between the
                    ** View Controller & the Toolbar */
                    marginBottom = (-4).px
                }
                mLinearProgress()
            }
        }
    }

}