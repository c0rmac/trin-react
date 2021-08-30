package com.trinitcore.trinreact.ui.wrapper.reactfullpage

import react.RBuilder
import react.RHandler
import styled.StyledProps

enum class FullPageScrollMode {
    FULL_PAGE, NORMAL
}

interface FullPageProps : StyledProps {

    var controls: Boolean
    var initialSlide: Int
    var duration: Int
    var beforeChange: () -> Unit
    var afterChange: () -> Unit
    var scrollMode: String

}

fun RBuilder.fullPage(
    controls: Boolean = false,
    initialSlide: Int = 0,
    duration: Int = 700,
    beforeChange: () -> Unit = {  },
    afterChange: () -> Unit = {  },
    scrollMode: FullPageScrollMode = FullPageScrollMode.FULL_PAGE,
    handler: RHandler<FullPageProps>
) = child(FullPage::class) {
    attrs.controls = controls
    attrs.initialSlide = initialSlide
    attrs.duration = duration
    attrs.beforeChange = beforeChange
    attrs.afterChange = afterChange
    attrs.scrollMode = when (scrollMode) {
        FullPageScrollMode.FULL_PAGE -> "full-page"
        FullPageScrollMode.NORMAL -> "normal"
    }

    handler.invoke(this)
}