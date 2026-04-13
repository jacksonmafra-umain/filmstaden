package com.filmstaden.app.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/**
 * Returns a modifier that marks the receiver as a shared element participating in
 * the navigation transition identified by [key]. When either the SharedTransitionLayout
 * scope or the NavDisplay AnimatedContent scope is unavailable, returns [Modifier]
 * unchanged so callers can apply it without conditional logic.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun sharedElementModifier(key: String): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return Modifier
    val animScope = LocalNavAnimatedContentScope.current
    return with(sharedScope) {
        Modifier.sharedElement(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animScope
        )
    }
}
