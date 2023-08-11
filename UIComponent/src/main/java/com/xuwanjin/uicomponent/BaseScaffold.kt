package com.xuwanjin.uicomponent

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 *  the base scaffold for all screen,
 *  it should three layout: top, content, bottom. all  of them have default value,
 *
 */
@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    topBarLayout: @Composable () -> Unit = {},
    bottomBarLayout: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        contentColor = Color.Transparent,
        content = {
            it.calculateBottomPadding()
            content()
        },
        bottomBar = bottomBarLayout,
        topBar = topBarLayout
    )
}

