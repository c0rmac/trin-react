package com.trinitcore.trinreact.ui.app.material.comp.icon

import com.ccfraser.muirwik.components.MIconProps
import kotlinx.html.HTMLTag
import react.*
import react.dom.RDOMBuilder
import react.dom.svg
import react.dom.tag

inline fun RBuilder.custom(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit): ReactElement = tag(block) {
    HTMLTag(tagName, it, mapOf(), null, inlineTag = true, emptyTag = false) // I dont know yet what the last 3 params mean... to lazy to look it up
}

class LocationAddIcon : RComponent<MIconProps, RState>() {

    override fun RBuilder.render() {
        svg {
            attrs["class"] = "MuiSvgIcon-root"
            attrs["viewBox"] = "0 0 24 24"
            custom("path") {
                attrs["d"] = "M 12,2 C 8.13,2 5,5.13 5,9 5,14.25 12,22 12,22 6.7443835,16.124254 16.738671,13.334548 19,9 20.790022,5.56886 15.87,2 12,2 Z m 0,9.5 C 10.62,11.5 9.5,10.38 9.5,9 9.5,7.62 10.62,6.5 12,6.5 c 1.38,0 2.5,1.12 2.5,2.5 0,1.38 -1.12,2.5 -2.5,2.5 z"
            }
            custom("path") {
                //attrs["style"] = "stroke-width:0.489382"
                attrs["d"] = "m 21.464518,19.247779 h -3.71993 v 3.615905 h -1.239977 v -3.615905 h -3.71993 v -1.2053 h 3.71993 v -3.615906 h 1.239977 v 3.615906 h 3.71993 z"
            }
        }
    }

}
/*
fun RBuilder.locationAddIcon(className: String? = null, addAsChild: Boolean = true, handler: StyledHandler<MIconProps>? = null): ReactElement {
    val builder = StyledElementBuilder<P>(component)
    handler(builder)
    return if (addAsChild) child(builder.create()) else builder.create()
    return if (addAsChild) child(LocationAddIcon::class) {  } else Styled.createElement(LocationAddIcon::class.rClass, CSSBuilder(), jsObject(), emptyList())
}
 */