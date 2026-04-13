package com.filmstaden.app.ui.tooling

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation for Filmstaden composables. Renders the
 * preview in light and dark mode, plus a large-font dark variant — covering
 * the most common visual regressions in one shot.
 *
 * Usage:
 *   @FsPreview
 *   @Composable
 *   private fun MyComponentPreview() {
 *     PreviewBox { MyComponent() }
 *   }
 */
@Preview(
    name = "Dark",
    group = "Dark",
    showBackground = true,
    backgroundColor = 0xFF0A0A0A,
    uiMode = UI_MODE_NIGHT_YES,
    fontScale = 1f
)
@Preview(
    name = "Dark - Large font",
    group = "Dark",
    showBackground = true,
    backgroundColor = 0xFF0A0A0A,
    uiMode = UI_MODE_NIGHT_YES,
    fontScale = 1.35f
)
@Preview(
    name = "Light",
    group = "Light",
    showBackground = true,
    backgroundColor = 0xFFEEEEEE,
    uiMode = UI_MODE_NIGHT_NO,
    fontScale = 1f
)
annotation class FsPreview

/**
 * Single full-screen device preview — used for entire screens that need to
 * render with realistic dimensions.
 */
@Preview(
    name = "Pixel 8 — Dark",
    showBackground = true,
    backgroundColor = 0xFF0A0A0A,
    uiMode = UI_MODE_NIGHT_YES,
    device = "spec:width=412dp,height=915dp,dpi=420"
)
annotation class FsScreenPreview
