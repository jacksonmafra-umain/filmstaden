package com.filmstaden.app.ui.screens.more

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmstaden.app.ui.theme.BgCard
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.FsRedGradient
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import com.filmstaden.app.ui.theme.TextSubtle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .verticalScroll(rememberScrollState())
    ) {
        // Red gradient header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(FsRedGradient)
                .padding(top = 56.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FILMSTADEN", color = Color.White.copy(alpha = 0.85f), fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Icon(Icons.Outlined.Settings, "Settings", tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(22.dp))
            }

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f))
                    .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.4f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Text(state.profile.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(state.profile.email, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(state.profile.orders.toString(), "Orders")
                StatItem(state.profile.points.toString(), "Points")
                StatItem("${state.profile.spentKr} kr", "Spent")
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionLabel("GENERAL")
            MenuItem(Icons.Outlined.AccountCircle, "Account Information", "Change your account info")
            MenuItem(Icons.Outlined.Lock, "Password", "Change your password")
            MenuItem(Icons.Outlined.CreditCard, "Payment Methods", "Add your credit & debit cards")
            MenuItem(Icons.Outlined.Place, "Favorite Cinemas", "Change your saved cinemas")
            MenuItem(Icons.Outlined.CardGiftcard, "Invite Friends", "Get 59 kr for each invitation!")

            Spacer(Modifier.height(12.dp))
            SectionLabel("NOTIFICATIONS")
            NotificationsRow(
                enabled = state.notificationsEnabled,
                onToggle = viewModel::toggleNotifications
            )
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        color = TextSubtle,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp
    )
}

@Composable
private fun MenuItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgCard)
            .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(12.dp))
            .clickable { }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FsRed.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = FsRed, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = TextMuted, fontSize = 11.sp)
        }
        Icon(Icons.Filled.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun NotificationsRow(enabled: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgCard)
            .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(12.dp))
            .clickable { onToggle() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FsRed.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Notifications, null, tint = FsRed, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("Notifications", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(
                if (enabled) "Daily updates enabled" else "Notifications off",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
        AnimatedToggle(enabled = enabled)
    }
}

@Composable
private fun AnimatedToggle(enabled: Boolean) {
    val bg by animateColorAsState(
        targetValue = if (enabled) FsRed else BgDark,
        label = "toggleBg"
    )
    val knobOffset by animateDpAsState(
        targetValue = if (enabled) 22.dp else 2.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "toggleKnob"
    )
    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(CircleShape)
            .background(bg)
            .border(BorderStroke(1.dp, BorderSubtle), CircleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}
