package com.trinitcore.trinreact.ui.app.material

data class DrawerItem(val title: String, val link: String? = null, val action: (() -> Unit)? = null)