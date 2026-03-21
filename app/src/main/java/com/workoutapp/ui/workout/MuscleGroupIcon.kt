package com.workoutapp.ui.workout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.workoutapp.data.model.BodyRegion
import com.workoutapp.data.model.MuscleGroup

/**
 * A horizontal row of Ripperdoc-style cyberpunk icons, one per BodyRegion targeted by this exercise.
 * Like a game stats row — angular geometric line art in the group's accent colour.
 */
@Composable
fun MuscleRegionIcons(
    group: MuscleGroup,
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    spacing: Dp = 6.dp
) {
    Row(modifier = modifier) {
        group.bodyRegions.forEachIndexed { idx, region ->
            if (idx > 0) Spacer(Modifier.width(spacing))
            BodyRegionIcon(region = region, color = group.color, sizeDp = iconSize)
        }
    }
}

@Composable
private fun BodyRegionIcon(
    region: BodyRegion,
    color: Color,
    sizeDp: Dp
) {
    Canvas(modifier = Modifier.size(sizeDp)) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        when (region) {
            BodyRegion.CHEST        -> drawChestIcon(color, stroke)
            BodyRegion.TRICEPS      -> drawTricepsIcon(color, stroke)
            BodyRegion.UPPER_BACK   -> drawUpperBackIcon(color, stroke)
            BodyRegion.BICEPS       -> drawBicepsIcon(color, stroke)
            BodyRegion.QUADS        -> drawQuadsIcon(color, stroke)
            BodyRegion.HAMSTRINGS   -> drawHamstringsIcon(color, stroke)
            BodyRegion.GLUTES       -> drawGlutesIcon(color, stroke)
            BodyRegion.ABS          -> drawAbsIcon(color, stroke)
            BodyRegion.SHOULDERS    -> drawShouldersIcon(color, stroke)
            BodyRegion.LOWER_BACK   -> drawLowerBackIcon(color, stroke)
        }
    }
}

// ── CHEST: two angular pec wedges radiating from a centre line ────────────────
private fun DrawScope.drawChestIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    val cx = w * 0.5f; val mid = h * 0.52f

    // Centre sternum line
    drawLine(color, Offset(cx, h * 0.2f), Offset(cx, mid), stroke.width, StrokeCap.Round)

    // Left pec — angular wedge
    val leftPec = Path().apply {
        moveTo(cx, h * 0.22f)
        lineTo(w * 0.12f, h * 0.35f)
        lineTo(w * 0.18f, mid)
        lineTo(cx, mid)
        close()
    }
    drawPath(leftPec, color = color, style = stroke)

    // Right pec
    val rightPec = Path().apply {
        moveTo(cx, h * 0.22f)
        lineTo(w * 0.88f, h * 0.35f)
        lineTo(w * 0.82f, mid)
        lineTo(cx, mid)
        close()
    }
    drawPath(rightPec, color = color, style = stroke)

    // Lower boundary line
    drawLine(color, Offset(w * 0.18f, mid), Offset(w * 0.82f, mid), stroke.width, StrokeCap.Round)
}

// ── TRICEPS: angular horseshoe U-shape (back of upper arm) ───────────────────
private fun DrawScope.drawTricepsIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    val path = Path().apply {
        // Horseshoe: open at bottom-left/right, rounded at top
        moveTo(w * 0.2f, h * 0.75f)
        lineTo(w * 0.2f, h * 0.35f)
        cubicTo(w * 0.2f, h * 0.1f, w * 0.8f, h * 0.1f, w * 0.8f, h * 0.35f)
        lineTo(w * 0.8f, h * 0.75f)
    }
    drawPath(path, color = color, style = stroke)
    // Crossbar suggesting muscle belly
    drawLine(color, Offset(w * 0.2f, h * 0.52f), Offset(w * 0.8f, h * 0.52f), stroke.width * 0.6f, StrokeCap.Round)
}

// ── UPPER BACK: V-taper inverted triangle (lats) ─────────────────────────────
private fun DrawScope.drawUpperBackIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    val path = Path().apply {
        moveTo(w * 0.1f, h * 0.2f)         // top-left shoulder
        lineTo(w * 0.9f, h * 0.2f)         // top-right shoulder
        lineTo(w * 0.5f, h * 0.82f)        // bottom-centre (V-point)
        close()
    }
    drawPath(path, color = color, style = stroke)
    // Horizontal bar at shoulders for traps
    drawLine(color, Offset(w * 0.1f, h * 0.2f), Offset(w * 0.9f, h * 0.2f), stroke.width, StrokeCap.Round)
    // Centre spine line
    drawLine(color, Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, h * 0.6f), stroke.width * 0.6f, StrokeCap.Round)
}

// ── BICEPS: peaked flex-arc (front of upper arm) ─────────────────────────────
private fun DrawScope.drawBicepsIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    // Peaked arch
    val path = Path().apply {
        moveTo(w * 0.12f, h * 0.7f)
        cubicTo(w * 0.05f, h * 0.35f, w * 0.35f, h * 0.1f, w * 0.5f, h * 0.18f)
        cubicTo(w * 0.65f, h * 0.1f, w * 0.95f, h * 0.35f, w * 0.88f, h * 0.7f)
    }
    drawPath(path, color = color, style = stroke)
    // Base line
    drawLine(color, Offset(w * 0.12f, h * 0.7f), Offset(w * 0.88f, h * 0.7f), stroke.width, StrokeCap.Round)
}

// ── QUADS: angular front-of-thigh rectangle with centre crease ───────────────
private fun DrawScope.drawQuadsIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    // Outer shape — slightly tapered thigh
    val path = Path().apply {
        moveTo(w * 0.22f, h * 0.18f)
        lineTo(w * 0.78f, h * 0.18f)
        lineTo(w * 0.72f, h * 0.82f)
        lineTo(w * 0.28f, h * 0.82f)
        close()
    }
    drawPath(path, color = color, style = stroke)
    // Centre crease
    drawLine(color, Offset(w * 0.5f, h * 0.18f), Offset(w * 0.5f, h * 0.82f), stroke.width * 0.55f, StrokeCap.Round)
    // Tear-drop sweep line (vastus lateralis)
    drawLine(color, Offset(w * 0.72f, h * 0.18f), Offset(w * 0.5f, h * 0.55f), stroke.width * 0.55f, StrokeCap.Round)
}

// ── HAMSTRINGS: angular back-of-thigh with two muscle-belly lines ─────────────
private fun DrawScope.drawHamstringsIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    val path = Path().apply {
        moveTo(w * 0.2f, h * 0.18f)
        lineTo(w * 0.8f, h * 0.18f)
        lineTo(w * 0.74f, h * 0.82f)
        lineTo(w * 0.26f, h * 0.82f)
        close()
    }
    drawPath(path, color = color, style = stroke)
    // Two belly lines (biceps femoris + semitendinosus)
    drawLine(color, Offset(w * 0.37f, h * 0.22f), Offset(w * 0.33f, h * 0.78f), stroke.width * 0.55f, StrokeCap.Round)
    drawLine(color, Offset(w * 0.63f, h * 0.22f), Offset(w * 0.67f, h * 0.78f), stroke.width * 0.55f, StrokeCap.Round)
}

// ── GLUTES: round-topped rectangle (butt shape) ───────────────────────────────
private fun DrawScope.drawGlutesIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    val path = Path().apply {
        // Rounded top (two glute mounds), straight bottom
        moveTo(w * 0.15f, h * 0.55f)
        lineTo(w * 0.15f, h * 0.8f)
        lineTo(w * 0.85f, h * 0.8f)
        lineTo(w * 0.85f, h * 0.55f)
        cubicTo(w * 0.85f, h * 0.18f, w * 0.6f, h * 0.18f, w * 0.5f, h * 0.35f)
        cubicTo(w * 0.4f, h * 0.18f, w * 0.15f, h * 0.18f, w * 0.15f, h * 0.55f)
    }
    drawPath(path, color = color, style = stroke)
    // Centre cleft
    drawLine(color, Offset(w * 0.5f, h * 0.35f), Offset(w * 0.5f, h * 0.8f), stroke.width * 0.55f, StrokeCap.Round)
}

// ── ABS: 3 stacked horizontal bars inside a box ───────────────────────────────
private fun DrawScope.drawAbsIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    // Outer border
    val border = Path().apply {
        addRect(Rect(w * 0.15f, h * 0.12f, w * 0.85f, h * 0.88f))
    }
    drawPath(border, color = color, style = stroke)
    // Centre crease (linea alba)
    drawLine(color, Offset(w * 0.5f, h * 0.12f), Offset(w * 0.5f, h * 0.88f), stroke.width * 0.5f, StrokeCap.Round)
    // 3 horizontal bands
    val rows = listOf(0.36f, 0.56f, 0.74f)
    rows.forEach { y ->
        drawLine(color, Offset(w * 0.15f, h * y), Offset(w * 0.85f, h * y), stroke.width * 0.7f, StrokeCap.Round)
    }
}

// ── SHOULDERS: two outward delta caps ─────────────────────────────────────────
private fun DrawScope.drawShouldersIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    // Left deltoid cap triangle
    val leftDelt = Path().apply {
        moveTo(w * 0.08f, h * 0.7f)
        lineTo(w * 0.08f, h * 0.3f)
        lineTo(w * 0.38f, h * 0.22f)
        lineTo(w * 0.38f, h * 0.62f)
        close()
    }
    drawPath(leftDelt, color = color, style = stroke)
    // Right deltoid cap triangle
    val rightDelt = Path().apply {
        moveTo(w * 0.92f, h * 0.7f)
        lineTo(w * 0.92f, h * 0.3f)
        lineTo(w * 0.62f, h * 0.22f)
        lineTo(w * 0.62f, h * 0.62f)
        close()
    }
    drawPath(rightDelt, color = color, style = stroke)
    // Connecting top line (acromion)
    drawLine(color, Offset(w * 0.38f, h * 0.22f), Offset(w * 0.62f, h * 0.22f), stroke.width, StrokeCap.Round)
}

// ── LOWER BACK: bold horizontal bar with corner bracket marks ─────────────────
private fun DrawScope.drawLowerBackIcon(color: Color, stroke: Stroke) {
    val w = size.width; val h = size.height
    // Main bar
    drawLine(color, Offset(w * 0.12f, h * 0.5f), Offset(w * 0.88f, h * 0.5f), stroke.width * 1.4f, StrokeCap.Round)
    // Top bracket arms
    drawLine(color, Offset(w * 0.12f, h * 0.28f), Offset(w * 0.12f, h * 0.5f), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(w * 0.88f, h * 0.28f), Offset(w * 0.88f, h * 0.5f), stroke.width, StrokeCap.Round)
    // Bottom bracket arms
    drawLine(color, Offset(w * 0.12f, h * 0.5f), Offset(w * 0.12f, h * 0.72f), stroke.width, StrokeCap.Round)
    drawLine(color, Offset(w * 0.88f, h * 0.5f), Offset(w * 0.88f, h * 0.72f), stroke.width, StrokeCap.Round)
    // Erector spinae — two flanking lines
    drawLine(color, Offset(w * 0.35f, h * 0.28f), Offset(w * 0.35f, h * 0.72f), stroke.width * 0.55f, StrokeCap.Round)
    drawLine(color, Offset(w * 0.65f, h * 0.28f), Offset(w * 0.65f, h * 0.72f), stroke.width * 0.55f, StrokeCap.Round)
}
