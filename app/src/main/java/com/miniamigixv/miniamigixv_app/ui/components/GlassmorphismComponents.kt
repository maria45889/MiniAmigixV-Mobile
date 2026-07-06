package com.miniamigixv.miniamigixv_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.miniamigixv.miniamigixv_app.ui.theme.DarkBackground
import com.miniamigixv.miniamigixv_app.ui.theme.DarkSurface
import com.miniamigixv.miniamigixv_app.ui.theme.NeonBlue
import com.miniamigixv.miniamigixv_app.ui.theme.NeonViolet

// Glassmorphism panel inspired by web design
// --glass-bg: rgba(18, 18, 26, 0.7)
// --glass-border: rgba(255, 255, 255, 0.1)
// --shadow-soft: 0 4px 20px rgba(0, 0, 0, 0.3)
// --shadow-glow: 0 0 30px rgba(139, 92, 246, 0.15)

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = Color(0xFF12121A).copy(alpha = 0.7f)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = Color(0xFF12121A).copy(alpha = 0.6f)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun NeonGlowBox(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonViolet,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun GradientBorderBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(NeonViolet, NeonBlue)
                )
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius - 2.dp))
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            content()
        }
    }
}
