package com.trinitcore.trinreact.ui.wrapper.reactphotogallery

import com.ccfraser.muirwik.components.createStyled
import react.RBuilder
import react.RComponent
import react.RState
import styled.StyledProps

interface GalleryProps : StyledProps {
    var photos: Array<Photo>
}

@JsModule("react-photo-gallery") private external val reactPhotoGalleryModule: dynamic
@Suppress("UnsafeCastFromDynamic") private val reactPhotoGalleryComponent: RComponent<GalleryProps, RState> = reactPhotoGalleryModule.default

fun RBuilder.gallery(photos: Array<Photo>) = createStyled(reactPhotoGalleryComponent) {
    attrs.photos = photos
}