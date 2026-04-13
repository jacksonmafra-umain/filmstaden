package com.filmstaden.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filmstaden.app.navigation.BottomTab
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary

/**
 * Animated 3-tab bottom bar with a morphing pill indicator that slides
 * between tabs using spring physics. Inspired by the showcase project's
 * MenuBottomBar pattern.
 */
@Composable
fun AnimatedTabBar(
    selected: BottomTab,
    onSelect: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = BottomTab.entries
    val density = LocalDensity.current
    var itemWidthPx by remember { mutableStateOf(0f) }

    val selectedIndex = tabs.indexOf(selected).coerceAtLeast(0)
    val indicatorOffset by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tabIndicatorOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BgDark)
    ) {
        // top hairline border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderSubtle)
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 10.dp, bottom = 20.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sliding pill indicator
            if (itemWidthPx > 0f) {
                val pillWidthDp = with(density) { (itemWidthPx * 0.72f).toDp() }
                val xOffsetDp = with(density) {
                    (itemWidthPx * indicatorOffset + (itemWidthPx - itemWidthPx * 0.72f) / 2f).toDp()
                }
                Box(
                    modifier = Modifier
                        .width(pillWidthDp)
                        .height(48.dp)
                        .graphicsLayer {
                            translationX = with(density) { xOffsetDp.toPx() }
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(FsRed.copy(alpha = 0.14f))
                )
                // Spacer to push Row children to the right of the indicator overlay
                Spacer(modifier = Modifier.width(0.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .graphicsLayer {
                        // Prevents the indicator from being consumed as background
                        translationX = 0f
                    },
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selected
                    val color by animateColorAsState(
                        targetValue = if (isSelected) FsRed else TextMuted,
                        animationSpec = tween(220),
                        label = "tabColor-${tab.name}"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.12f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "tabScale-${tab.name}"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .onGloballyPositioned { coords ->
                                if (itemWidthPx == 0f) itemWidthPx = coords.size.width.toFloat()
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onSelect(tab) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = color,
                            modifier = Modifier
                                .size(22.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = tab.label,
                            color = color,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

