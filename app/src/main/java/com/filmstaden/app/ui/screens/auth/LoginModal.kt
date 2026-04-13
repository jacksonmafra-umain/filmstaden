package com.filmstaden.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filmstaden.app.ui.components.FilmstadenButton
import com.filmstaden.app.ui.theme.BgCard
import com.filmstaden.app.ui.theme.BgCardLight
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import com.filmstaden.app.ui.theme.TextSubtle

@Composable
fun LoginModal(
    visible: Boolean,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(250)),
            exit = fadeOut(tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(
                initialScale = 0.9f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(tween(220)),
            exit = scaleOut(targetScale = 0.92f, animationSpec = tween(220)) + fadeOut(tween(180)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(BgDark)
                    .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(24.dp))
                    .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BgCardLight)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Close, "Close", tint = TextMuted, modifier = Modifier.size(18.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(FsRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text("F", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                }
                Text("Welcome to Filmstaden", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Sign in to book tickets, earn points, and manage your cinema experience.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                InputField(label = "Email", placeholder = "your@email.com", icon = Icons.Filled.Mail)
                InputField(label = "Password", placeholder = "••••••••", icon = Icons.Filled.Lock, trailing = Icons.Filled.VisibilityOff)

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text("Forgot password?", color = FsRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                FilmstadenButton(text = "Sign In", height = 50.dp, onClick = onSignIn)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f).height(1.dp).background(BorderSubtle))
                    Text("or", color = TextMuted, fontSize = 12.sp)
                    Box(modifier = Modifier.weight(1f).height(1.dp).background(BorderSubtle))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(BgCard)
                        .border(BorderStroke(1.5.dp, BorderSubtle), RoundedCornerShape(14.dp))
                        .clickable { onCreateAccount() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Create Account", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Text(
                    "By continuing, you agree to Filmstaden's Terms of Service and Privacy Policy.",
                    color = TextSubtle,
                    fontSize = 10.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BgCard)
                .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(10.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            Text(placeholder, color = TextSubtle, fontSize = 14.sp, modifier = Modifier.weight(1f))
            if (trailing != null) {
                Icon(trailing, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}
