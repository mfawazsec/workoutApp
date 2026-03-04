package com.workoutapp.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import com.workoutapp.data.model.BodyRegion
import com.workoutapp.data.model.MuscleGroup

/**
 * Front + back body silhouette with highlighted muscle regions.
 *
 * The body is drawn as a filled, smooth silhouette. Muscle regions are
 * semi-transparent colour overlays clipped to the body bounds.
 *
 * All coordinates are in a 60×160 normalised space, scaled to canvas at draw time.
 */
@Composable
fun BodySilhouette(
    selectedGroups: Set<MuscleGroup>,
    onRegionTap: ((MuscleGroup) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        BodyView(
            isFront = true,
            selectedGroups = selectedGroups,
            onRegionTap = onRegionTap,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        BodyView(
            isFront = false,
            selectedGroups = selectedGroups,
            onRegionTap = onRegionTap,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

// ── Normalised viewport: 60 wide × 160 tall ──────────────────────────────────
private const val VW = 60f
private const val VH = 160f

@Composable
private fun BodyView(
    isFront: Boolean,
    selectedGroups: Set<MuscleGroup>,
    onRegionTap: ((MuscleGroup) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val regionToGroup: Map<BodyRegion, MuscleGroup> = remember {
        buildMap { MuscleGroup.entries.forEach { g -> g.bodyRegions.forEach { r -> put(r, g) } } }
    }

    val tapMod = if (onRegionTap != null) {
        modifier.pointerInput(selectedGroups) {
            detectTapGestures { tap ->
                val sx = size.width / VW
                val sy = size.height / VH
                val regions = if (isFront) frontRegions else backRegions
                for ((region, pathFn) in regions) {
                    val bounds = pathFn(sx, sy).getBounds()
                    if (bounds.inflate(4f).contains(tap)) {
                        regionToGroup[region]?.let { onRegionTap(it) }
                        break
                    }
                }
            }
        }
    } else modifier

    Canvas(modifier = tapMod) {
        val sx = size.width / VW
        val sy = size.height / VH

        val bodyFill = Color(0xFF232323)
        val bodyStroke = Color(0xFF3D3D3D)
        val strokeWidth = 1f

        // ── 1. Draw filled body silhouette ────────────────────────────────
        val body = fullBodyPath(isFront, sx, sy)
        drawPath(body, color = bodyFill)
        drawPath(body, color = bodyStroke, style = Stroke(width = strokeWidth))

        // ── 2. Clip all muscle overlays to the body shape ─────────────────
        clipPath(body) {
            val regions = if (isFront) frontRegions else backRegions
            for ((region, pathFn) in regions) {
                val group = regionToGroup[region] ?: continue
                val path = pathFn(sx, sy)
                if (selectedGroups.contains(group)) {
                    drawPath(path, color = group.color.copy(alpha = 0.70f))
                } else {
                    // Subtle inactive tint so body regions are visible even unselected
                    drawPath(path, color = Color(0xFF2E2E2E))
                }
            }
        }

        // ── 3. Re-draw body outline on top ────────────────────────────────
        drawPath(body, color = bodyStroke, style = Stroke(width = strokeWidth))
    }
}

// ── Full body silhouette path ─────────────────────────────────────────────────
// One smooth closed path representing the human form in the 60×160 viewport.

private fun fullBodyPath(isFront: Boolean, sx: Float, sy: Float): Path {
    // The same silhouette works for front and back — only overlays differ.
    return Path().apply {
        val cx = 30f // horizontal centre

        // Head — circle approximated with cubic bezier
        val hCx = cx * sx; val hCy = 11f * sy; val hR = 8f * sx
        moveTo(hCx, (hCy - hR))
        cubicTo(hCx + hR * 0.55f, hCy - hR, hCx + hR, hCy - hR * 0.55f, hCx + hR, hCy)
        cubicTo(hCx + hR, hCy + hR * 0.55f, hCx + hR * 0.55f, hCy + hR, hCx, hCy + hR)
        cubicTo(hCx - hR * 0.55f, hCy + hR, hCx - hR, hCy + hR * 0.55f, hCx - hR, hCy)
        cubicTo(hCx - hR, hCy - hR * 0.55f, hCx - hR * 0.55f, hCy - hR, hCx, hCy - hR)
        close()

        // Torso + limbs — one connected closed path
        // Start at left shoulder outer
        moveTo(11f * sx, 27f * sy)

        // Left arm outer edge down to left wrist
        cubicTo(8f * sx,  28f * sy,   6f * sx, 35f * sy,   7f * sx, 46f * sy)
        lineTo(9f * sx,  60f * sy)   // left forearm outer
        lineTo(11f * sx, 60f * sy)   // left wrist

        // Left arm inner edge back up
        lineTo(12f * sx, 48f * sy)
        cubicTo(13f * sx, 37f * sy,  14f * sx, 30f * sy,  16f * sx, 28f * sy)

        // Left side of torso down to left hip
        lineTo(16f * sx, 28f * sy)
        cubicTo(15f * sx, 50f * sy,  14f * sx, 60f * sy,  16f * sx, 72f * sy)

        // Left hip curve down to left knee
        cubicTo(17f * sx, 76f * sy,  18f * sx, 78f * sy,  19f * sx, 80f * sy)
        lineTo(19f * sx, 110f * sy)  // left leg inner to knee
        lineTo(17f * sx, 155f * sy)  // left ankle inner

        // Left foot
        lineTo(16f * sx, 158f * sy)
        lineTo(24f * sx, 158f * sy)
        lineTo(24f * sx, 155f * sy)

        // Left leg outer
        lineTo(22f * sx, 110f * sy)
        lineTo(23f * sx, 80f * sy)

        // Hip gap to right leg
        cubicTo(26f * sx, 76f * sy,  34f * sx, 76f * sy,  37f * sx, 80f * sy)

        // Right leg outer
        lineTo(38f * sx, 110f * sy)
        lineTo(36f * sx, 155f * sy)
        lineTo(36f * sx, 158f * sy)
        lineTo(44f * sx, 158f * sy)
        lineTo(43f * sx, 155f * sy)

        // Right leg inner up to right hip
        lineTo(41f * sx, 110f * sy)
        lineTo(41f * sx, 80f * sy)
        cubicTo(42f * sx, 78f * sy,  43f * sx, 76f * sy,  44f * sx, 72f * sy)

        // Right side of torso up to right shoulder
        cubicTo(46f * sx, 60f * sy,  45f * sx, 50f * sy,  44f * sx, 28f * sy)

        // Right arm inner edge down
        cubicTo(46f * sx, 30f * sy,  47f * sx, 37f * sy,  48f * sx, 48f * sy)
        lineTo(49f * sx, 60f * sy)   // right wrist inner

        // Right wrist / hand
        lineTo(51f * sx, 60f * sy)
        lineTo(53f * sx, 46f * sy)

        // Right arm outer edge up to right shoulder
        cubicTo(54f * sx, 35f * sy,  52f * sx, 28f * sy,  49f * sx, 27f * sy)

        // Shoulder top — across to left
        cubicTo(43f * sx, 25f * sy,  17f * sx, 25f * sy,  11f * sx, 27f * sy)

        close()
    }
}

// ── Region definitions ────────────────────────────────────────────────────────

private val frontRegions: List<Pair<BodyRegion, (Float, Float) -> Path>> = listOf(
    BodyRegion.SHOULDERS    to ::rShoulders,
    BodyRegion.CHEST        to ::rChest,
    BodyRegion.BICEPS       to ::rFrontBiceps,
    BodyRegion.TRICEPS      to ::rFrontTriceps,
    BodyRegion.ABS          to ::rAbs,
    BodyRegion.QUADS        to ::rQuads,
)

private val backRegions: List<Pair<BodyRegion, (Float, Float) -> Path>> = listOf(
    BodyRegion.SHOULDERS    to ::rShoulders,
    BodyRegion.UPPER_BACK   to ::rUpperBack,
    BodyRegion.BICEPS       to ::rFrontBiceps,
    BodyRegion.TRICEPS      to ::rBackTriceps,
    BodyRegion.LOWER_BACK   to ::rLowerBack,
    BodyRegion.GLUTES       to ::rGlutes,
    BodyRegion.HAMSTRINGS   to ::rHamstrings,
)

// ── Muscle region paths (60×160 viewport) ─────────────────────────────────────

private fun rShoulders(sx: Float, sy: Float) = Path().apply {
    // Left deltoid cap
    moveTo(11f * sx, 27f * sy)
    cubicTo(8f * sx, 28f * sy, 6f * sx, 35f * sy, 9f * sx, 42f * sy)
    lineTo(14f * sx, 40f * sy)
    cubicTo(14f * sx, 33f * sy, 14f * sx, 29f * sy, 16f * sx, 27f * sy)
    close()
    // Right deltoid cap
    moveTo(49f * sx, 27f * sy)
    cubicTo(52f * sx, 28f * sy, 54f * sx, 35f * sy, 51f * sx, 42f * sy)
    lineTo(46f * sx, 40f * sy)
    cubicTo(46f * sx, 33f * sy, 46f * sx, 29f * sy, 44f * sx, 27f * sy)
    close()
}

private fun rChest(sx: Float, sy: Float) = Path().apply {
    // Left pec
    moveTo(16f * sx, 28f * sy)
    lineTo(29f * sx, 28f * sy)
    lineTo(29f * sx, 48f * sy)
    cubicTo(24f * sx, 52f * sy, 16f * sx, 48f * sy, 16f * sx, 44f * sy)
    close()
    // Right pec
    moveTo(31f * sx, 28f * sy)
    lineTo(44f * sx, 28f * sy)
    lineTo(44f * sx, 44f * sy)
    cubicTo(44f * sx, 48f * sy, 36f * sx, 52f * sy, 31f * sx, 48f * sy)
    close()
}

private fun rFrontBiceps(sx: Float, sy: Float) = Path().apply {
    // Left
    moveTo(9f * sx, 30f * sy)
    lineTo(13f * sx, 28f * sy)
    lineTo(14f * sx, 46f * sy)
    lineTo(10f * sx, 47f * sy)
    close()
    // Right
    moveTo(51f * sx, 30f * sy)
    lineTo(47f * sx, 28f * sy)
    lineTo(46f * sx, 46f * sy)
    lineTo(50f * sx, 47f * sy)
    close()
}

private fun rFrontTriceps(sx: Float, sy: Float) = Path().apply {
    // Barely visible from front — thin strip
    moveTo(7f * sx,  34f * sy)
    lineTo(9f * sx,  30f * sy)
    lineTo(10f * sx, 47f * sy)
    lineTo(7f * sx,  50f * sy)
    close()
    moveTo(53f * sx, 34f * sy)
    lineTo(51f * sx, 30f * sy)
    lineTo(50f * sx, 47f * sy)
    lineTo(53f * sx, 50f * sy)
    close()
}

private fun rAbs(sx: Float, sy: Float) = Path().apply {
    moveTo(16f * sx, 50f * sy)
    lineTo(44f * sx, 50f * sy)
    cubicTo(44f * sx, 72f * sy, 43f * sx, 74f * sy, 30f * sx, 76f * sy)
    cubicTo(17f * sx, 74f * sy, 16f * sx, 72f * sy, 16f * sx, 50f * sy)
    close()
}

private fun rQuads(sx: Float, sy: Float) = Path().apply {
    // Left quad
    moveTo(19f * sx, 80f * sy)
    lineTo(28f * sx, 79f * sy)
    lineTo(27f * sx, 115f * sy)
    lineTo(18f * sx, 116f * sy)
    close()
    // Right quad
    moveTo(41f * sx, 80f * sy)
    lineTo(32f * sx, 79f * sy)
    lineTo(33f * sx, 115f * sy)
    lineTo(42f * sx, 116f * sy)
    close()
}

private fun rUpperBack(sx: Float, sy: Float) = Path().apply {
    moveTo(16f * sx, 28f * sy)
    lineTo(44f * sx, 28f * sy)
    lineTo(43f * sx, 58f * sy)
    cubicTo(37f * sx, 62f * sy, 23f * sx, 62f * sy, 17f * sx, 58f * sy)
    close()
}

private fun rBackTriceps(sx: Float, sy: Float) = Path().apply {
    // Triceps dominate on the back of the arm
    moveTo(7f * sx,  30f * sy)
    lineTo(13f * sx, 27f * sy)
    lineTo(14f * sx, 48f * sy)
    lineTo(7f * sx,  52f * sy)
    close()
    moveTo(53f * sx, 30f * sy)
    lineTo(47f * sx, 27f * sy)
    lineTo(46f * sx, 48f * sy)
    lineTo(53f * sx, 52f * sy)
    close()
}

private fun rLowerBack(sx: Float, sy: Float) = Path().apply {
    moveTo(17f * sx, 60f * sy)
    lineTo(43f * sx, 60f * sy)
    lineTo(44f * sx, 76f * sy)
    lineTo(16f * sx, 76f * sy)
    close()
}

private fun rGlutes(sx: Float, sy: Float) = Path().apply {
    moveTo(16f * sx, 76f * sy)
    lineTo(44f * sx, 76f * sy)
    cubicTo(45f * sx, 88f * sy, 42f * sx, 95f * sy, 30f * sx, 97f * sy)
    cubicTo(18f * sx, 95f * sy, 15f * sx, 88f * sy, 16f * sx, 76f * sy)
    close()
}

private fun rHamstrings(sx: Float, sy: Float) = Path().apply {
    // Left hamstring
    moveTo(19f * sx, 97f * sy)
    lineTo(28f * sx, 96f * sy)
    lineTo(27f * sx, 132f * sy)
    lineTo(18f * sx, 133f * sy)
    close()
    // Right hamstring
    moveTo(41f * sx, 97f * sy)
    lineTo(32f * sx, 96f * sy)
    lineTo(33f * sx, 132f * sy)
    lineTo(42f * sx, 133f * sy)
    close()
}

// Helper — inflate Rect for easier hit-testing
private fun Rect.inflate(amount: Float): Rect =
    Rect(left - amount, top - amount, right + amount, bottom + amount)

private fun Rect.contains(offset: Offset): Boolean =
    offset.x in left..right && offset.y in top..bottom
