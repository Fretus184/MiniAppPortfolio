package com.nees.app.nesherportfolio.sys.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

data class WindowSize(
    val width: WindowType,
    val height: WindowType
)

enum class WindowType {
    Compact, Medium, Expanded
}

@Composable
fun rememberWindowSize() : WindowSize {
    val configuration = LocalConfiguration.current

    return WindowSize(
        width = when {
            configuration.screenWidthDp < 600 -> WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        },
        height = when {
            configuration.screenHeightDp < 600 -> WindowType.Compact
            configuration.screenHeightDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        }
    )
}

@Composable
fun ItemScreen(compactScreen:@Composable () -> Unit,
               mediumScreen:@Composable () -> Unit = {},
               expandedScreen:@Composable () -> Unit = {}) {
    val windowSize = rememberWindowSize()
    when(windowSize.width) {
        WindowType.Compact -> {
            compactScreen.invoke()
        }
        WindowType.Medium -> {
            mediumScreen.invoke()
        }
        else -> {
            expandedScreen.invoke()
        }
    }
}
