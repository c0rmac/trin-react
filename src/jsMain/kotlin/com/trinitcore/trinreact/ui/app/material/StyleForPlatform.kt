/** Post React JS App render injections */
package com.trinitcore.trinreact.ui.app.material

import styled.StyleSheet
import kotlinx.browser.window

object StyleForPlatform : StyleSheet(name = "style-for-platform", isStatic = true) {

    val platform = window.navigator.platform.lowercase()
    val isApple = platform.contains("iphone") || platform.contains("ipod") || platform.contains("ipad") ||
            platform.contains("mac")

    // val viewHolderContainer = if (isApple) "view-holder-container-ios" else "view-holder-container-generic"

}