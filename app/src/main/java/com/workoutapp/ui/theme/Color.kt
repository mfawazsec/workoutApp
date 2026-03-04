package com.workoutapp.ui.theme

import androidx.compose.ui.graphics.Color

// ── Atlas Neon Palette ───────────────────────────────────────────────────────
val NeonCyan            = Color(0xFF00E5FF)   // Primary neon cyan
val NeonCyanDim         = Color(0xFF0099AA)   // Dimmed cyan for muted accents
val NeonCyanGlow        = Color(0x4400E5FF)   // Glow/shadow (44 = 27% alpha)
val NeonOrange          = Color(0xFFFF6B47)   // PUSH accent
val NeonBlue            = Color(0xFF4A90D9)   // PULL accent
val NeonGreen           = Color(0xFF39FF14)   // LEGS accent (neon green)
val NeonPurple          = Color(0xFFBF5FFF)   // CORE+SHOULDERS accent

// ── Muscle Group Accent Colors (keeping for existing compatibility) ───────────
val PushColor           = NeonOrange
val PullColor           = NeonBlue
val LegsColor           = NeonGreen
val CoreShouldersColor  = NeonPurple

// ── Rep Zone Colors ──────────────────────────────────────────────────────────
val StrengthZoneColor   = Color(0xFFFFD93D)
val HypertrophyColor    = NeonGreen
val EnduranceZoneColor  = Color(0xFFFF9F45)
val OutOfRangeColor     = Color(0xFFFF4D4D)

// ── App Surface Palette (OLED Black) ─────────────────────────────────────────
val Background          = Color(0xFF000000)   // Pure OLED black
val CardSurface         = Color(0xFF0D0D0D)   // Slightly off-black for cards
val CardSurfaceElevated = Color(0xFF1A1A1A)   // Elevated card surface
val OnSurface           = Color(0xFFE8E8E8)
val OnSurfaceMuted      = Color(0xFF7A7A7A)
val InactiveChip        = Color(0xFF1C1C1C)
val Divider             = Color(0xFF1E1E1E)

// ── Material3 Scheme Seeds ────────────────────────────────────────────────────
val Primary             = NeonCyan
val OnPrimary           = Color(0xFF000000)
val PrimaryContainer    = Color(0xFF003340)
val OnPrimaryContainer  = NeonCyan
val Secondary           = Color(0xFF4A90D9)
val OnSecondary         = Color(0xFF000000)
val SecondaryContainer  = Color(0xFF00315C)
val OnSecondaryContainer= Color(0xFFD3E4FF)
val Tertiary            = NeonPurple
val OnTertiary          = Color(0xFF000000)
val TertiaryContainer   = Color(0xFF2A0050)
val OnTertiaryContainer = NeonPurple
val Error               = Color(0xFFFF4D4D)
val OnError             = Color(0xFF000000)
val Surface             = Color(0xFF000000)
val OnSurfaceColor      = Color(0xFFE8E8E8)
val SurfaceVariant      = Color(0xFF0D0D0D)
val OnSurfaceVariant    = Color(0xFFAAAAAA)
val Outline             = Color(0xFF1E1E1E)
val OutlineVariant      = Color(0xFF2A2A2A)
