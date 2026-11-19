package app.aaps.ui.compose.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The version label in the top right corner of the main screen.
 *
 * Hidden in this build: it draws nothing. The function stays so that the caller in `MainScreen`
 * does not have to change.
 *
 * @see VersionOverlayPreview
 */
@Composable
fun VersionOverlay(
    @Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier,
) {
}
