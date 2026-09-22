package com.omidsa596pwid

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider

// Common manual tap-to-refresh modifier
private fun createRefreshModifier(context: Context, receiverClass: Class<*>): GlanceModifier {
    val refreshIntent = Intent(context, receiverClass).apply {
        action = "com.omidsa596pwid.ACTION_MANUAL_REFRESH"
    }
    return GlanceModifier.clickable(actionSendBroadcast(refreshIntent))
}

/**
 * Hardware-accelerated Bitmap Canvas factory for Android Home Screen Glance widgets.
 * Renders multi-layer glowing neon, 3D glossy bubbles, 24K gold metallic borders,
 * and intricate texture backgrounds that RemoteViews cannot render with plain CSS/Compose colors.
 */
object WidgetBackgroundFactory {
    private val cache = mutableMapOf<String, Bitmap>()

    fun getForWidget(widgetId: String, w: Int = 640, h: Int = 180): Bitmap {
        return when (widgetId) {
            "neon_cyan_card", "neon-cyan", "neon_cyan" -> getNeonCyan(w, h)
            "luxury_dark", "luxury-dark" -> getLuxuryDark(w, h)
            "frosted_liquid", "frosted-liquid" -> getFrostedLiquid(w, h)
            "aurora_glass", "aurora-glass" -> getAuroraGlass(w, h)
            "cyber_gold_2x2", "cyber-gold", "cyber-gold-2x2", "cyber_gold" -> getCyberGold2x2(w, h)
            "diamond_prism", "diamond-prism" -> getDiamondPrism(w, h)
            "executive_leather", "executive-leather" -> getExecutiveLeather(w, h)
            "analog_celestial", "analog-celestial" -> getAnalogCelestial(w, h)
            "hologram_grid", "hologram-grid" -> getHologramGrid(w, h)
            "royal_emerald", "royal-emerald" -> getRoyalEmerald(w, h)
            "titanium_chronos", "titanium-chronos" -> getTitaniumChronos(w, h)
            "retro_digital", "retro-digital" -> getRetroDigital(w, h)
            "paper_clean", "paper-clean" -> getPaperClean(w, h)
            "split_horizon", "split-horizon" -> getSplitHorizon(w, h)
            "minimal_pill", "minimal-pill" -> getMinimalPill(w, h)
            "compact_classic", "compact-classic" -> getCompactClassic(w, h)
            "modern_capsule", "modern-capsule" -> getModernCapsule(w, h)
            "glass_celestial", "glass-celestial" -> getGlassCelestial(true, w, h)
            "calendar_tile", "calendar-tile" -> getCalendarTile(w, h)
            else -> getNeonCyan(w, h)
        }
    }

    fun getNeonCyan(w: Int = 640, h = 180): Bitmap = cache.getOrPut("neon_cyan") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        // 1. Deep Cyber Navy linear gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(0xFF030D19.toInt(), 0xFF081C30.toInt(), 0xFF020B14.toInt()),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        // 2. Electric Cyan Neon Glow Halo (Multi-pass intense dispersion)
        val glow1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 16f
            color = 0x3500F0FF.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, glow1)

        val glow2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 9f
            color = 0x7500F0FF.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, glow2)

        // 3. Crisp Neon Cyan Core stroke
        val core = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            color = 0xFF00F0FF.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, core)

        // 4. White-hot center highlight line
        val hotCore = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = 0xFFE0FFFF.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, hotCore)

        // 5. Cyan ambient corner flare
        val flare = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.12f, h * 0.5f, 120f,
                0x4500F0FF.toInt(), 0x00000000, Shader.TileMode.CLAMP)
        }
        canvas.drawCircle(w * 0.12f, h * 0.5f, 120f, flare)

        // 6. Cyber corner brackets (top-left, top-right, bottom-left, bottom-right)
        val bracketPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = 0xFF00F0FF.toInt()
        }
        canvas.drawLine(18f, 30f, 18f, 18f, bracketPaint)
        canvas.drawLine(18f, 18f, 30f, 18f, bracketPaint)
        canvas.drawLine(w - 30f, 18f, w - 18f, 18f, bracketPaint)
        canvas.drawLine(w - 18f, 18f, w - 18f, 30f, bracketPaint)
        canvas.drawLine(18f, h - 30f, 18f, h - 18f, bracketPaint)
        canvas.drawLine(18f, h - 18f, 30f, h - 18f, bracketPaint)
        canvas.drawLine(w - 30f, h - 18f, w - 18f, h - 18f, bracketPaint)
        canvas.drawLine(w - 18f, h - 30f, w - 18f, h - 18f, bracketPaint)

        bm
    }

    fun getLuxuryDark(w: Int = 640, h = 180): Bitmap = cache.getOrPut("luxury_dark") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        // 1. Obsidian crystal radial gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.3f, 0f, w * 0.9f,
                intArrayOf(0xFF221A12.toInt(), 0xFF0E1017.toInt(), 0xFF07080C.toInt()),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        // 2. 24K Metallic Brushed Gold Gradient Border
        val goldBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4.5f
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(
                    0xFF855806.toInt(),
                    0xFFFFE57F.toInt(),
                    0xFFD4AF37.toInt(),
                    0xFFFFF6B8.toInt(),
                    0xFFAA771C.toInt(),
                    0xFFFFE57F.toInt()
                ),
                floatArrayOf(0f, 0.2f, 0.45f, 0.65f, 0.85f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, goldBorder)

        // 3. Inner fine gold hairline
        val innerRect = RectF(14f, 14f, w - 14f, h - 14f)
        val innerStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = 0x44FFE57F.toInt()
        }
        canvas.drawRoundRect(innerRect, rad - 8f, rad - 8f, innerStroke)

        // 4. Gold micro-sparkles across obsidian surface
        val sparkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x88FFE57F.toInt() }
        canvas.drawCircle(w * 0.22f, h * 0.28f, 2f, sparkPaint)
        canvas.drawCircle(w * 0.55f, h * 0.72f, 1.5f, sparkPaint)
        canvas.drawCircle(w * 0.78f, h * 0.35f, 2f, sparkPaint)
        canvas.drawCircle(w * 0.88f, h * 0.68f, 1.8f, sparkPaint)
        bm
    }

    fun getFrostedLiquid(w: Int = 640, h = 180): Bitmap = cache.getOrPut("frosted_liquid") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        // 1. Translucent frosted glass gradient
        val glassPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, 0f, h.toFloat(),
                intArrayOf(0xEA334155.toInt(), 0xD01E293B.toInt(), 0xB00F172A.toInt()),
                floatArrayOf(0f, 0.6f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, glassPaint)

        // 2. Frosted glass rim highlight
        val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            shader = LinearGradient(0f, 0f, 0f, h.toFloat(),
                intArrayOf(0xEEFFFFFF.toInt(), 0x66FFFFFF.toInt()),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, rimPaint)

        // 3. 3D Glossy Liquid Water Bubbles & Droplets with specular highlights & refraction
        fun drawLiquidBubble(cx: Float, cy: Float, r: Float) {
            val bPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(cx, cy, r,
                    intArrayOf(0x70FFFFFF.toInt(), 0x3538BDF8.toInt(), 0x05000000),
                    floatArrayOf(0f, 0.65f, 1f),
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawCircle(cx, cy, r, bPaint)

            // Outer bubble refraction ring
            val bRim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 2.2f
                color = 0xCCFFFFFF.toInt()
            }
            canvas.drawCircle(cx, cy, r, bRim)

            // Specular crescent reflection arc at top-left
            val specPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 3f
                color = 0xFFFFFFFF.toInt()
            }
            val arcRect = RectF(cx - r * 0.75f, cy - r * 0.75f, cx + r * 0.35f, cy + r * 0.35f)
            canvas.drawArc(arcRect, 190f, 85f, false, specPaint)

            // Secondary crescent reflection arc at bottom-right
            val spec2Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 1.8f
                color = 0x99BAE6FD.toInt()
            }
            val arc2Rect = RectF(cx - r * 0.35f, cy - r * 0.35f, cx + r * 0.75f, cy + r * 0.75f)
            canvas.drawArc(arc2Rect, 15f, 70f, false, spec2Paint)

            // Small highlight sparkle dot
            val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() }
            canvas.drawCircle(cx + r * 0.35f, cy + r * 0.35f, r * 0.18f, dotPaint)
        }

        // Prominent 3D bubbles across widget area
        drawLiquidBubble(w * 0.14f, h * 0.52f, 28f)
        drawLiquidBubble(w * 0.28f, h * 0.76f, 18f)
        drawLiquidBubble(w * 0.48f, h * 0.22f, 15f)
        drawLiquidBubble(w * 0.68f, h * 0.82f, 16f)
        drawLiquidBubble(w * 0.82f, h * 0.38f, 26f)
        drawLiquidBubble(w * 0.92f, h * 0.68f, 17f)
        bm
    }

    fun getAuroraGlass(w: Int = 640, h = 180): Bitmap = cache.getOrPut("aurora_glass") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        // Aurora Borealis plasma wave gradient
        val auroraPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(
                    0xFF1E0A3C.toInt(),
                    0xFF4C1D95.toInt(),
                    0xFF0E7490.toInt(),
                    0xFF059669.toInt()
                ),
                floatArrayOf(0f, 0.35f, 0.7f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, auroraPaint)

        // Soft luminous nebula glow
        val nebulaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.65f, h * 0.5f, 160f,
                0x4522D3EE.toInt(), 0x00000000, Shader.TileMode.CLAMP)
        }
        canvas.drawCircle(w * 0.65f, h * 0.5f, 160f, nebulaPaint)

        // Crystal violet/cyan rim
        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = 0x99C084FC.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getCyberGold2x2(w: Int = 380, h = 380): Bitmap = cache.getOrPut("cyber_gold_2x2") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.5f, h * 0.35f, w * 0.8f,
                intArrayOf(0xFF3B2608.toInt(), 0xFF18151D.toInt(), 0xFF0D0E12.toInt()),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val orbit = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = 0x22F59E0B.toInt()
        }
        canvas.drawCircle(w * 0.5f, h * 0.35f, 75f, orbit)
        canvas.drawCircle(w * 0.5f, h * 0.35f, 120f, orbit)

        val goldRim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(0xFFF59E0B.toInt(), 0xFFFDE68A.toInt(), 0xFFB45309.toInt(), 0xFFFBBF24.toInt()),
                floatArrayOf(0f, 0.33f, 0.66f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, goldRim)
        bm
    }

    fun getDiamondPrism(w: Int = 640, h = 180): Bitmap = cache.getOrPut("diamond_prism") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(0xFF05172E.toInt(), 0xFF0B2E56.toInt(), 0xFF031020.toInt()),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val streakPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            shader = LinearGradient(0f, 0f, w * 0.5f, h.toFloat(),
                intArrayOf(0x0038BDF8, 0x2538BDF8.toInt(), 0x0038BDF8),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        val path = Path().apply {
            moveTo(w * 0.2f, 0f)
            lineTo(w * 0.45f, 0f)
            lineTo(w * 0.3f, h.toFloat())
            lineTo(w * 0.05f, h.toFloat())
            close()
        }
        canvas.drawPath(path, streakPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = 0xFF38BDF8.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getHologramGrid(w: Int = 640, h = 180): Bitmap = cache.getOrPut("hologram_grid") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF020E18.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        // Holographic grid matrix (horizontal + vertical lines)
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x2200F0FF.toInt()
            strokeWidth = 1.2f
        }
        var y = 14f
        while (y < h - 14f) {
            canvas.drawLine(14f, y, w - 14f, y, linePaint)
            y += 14f
        }
        var x = 14f
        while (x < w - 14f) {
            canvas.drawLine(x, 14f, x, h - 14f, linePaint)
            x += 24f
        }

        // Radar target circles on left
        val radarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.4f
            color = 0x3500F0FF.toInt()
        }
        canvas.drawCircle(w * 0.12f, h * 0.5f, 32f, radarPaint)
        canvas.drawCircle(w * 0.12f, h * 0.5f, 58f, radarPaint)

        // Bright neon cyan outer rim
        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.8f
            color = 0xFF00F0FF.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)

        // Holographic corner bracket markers
        val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = 0xFF00F0FF.toInt()
        }
        canvas.drawLine(16f, 26f, 16f, 16f, cornerPaint)
        canvas.drawLine(16f, 16f, 26f, 16f, cornerPaint)
        canvas.drawLine(w - 26f, 16f, w - 16f, 16f, cornerPaint)
        canvas.drawLine(w - 16f, 16f, w - 16f, 26f, cornerPaint)
        canvas.drawLine(16f, h - 26f, 16f, h - 16f, cornerPaint)
        canvas.drawLine(16f, h - 16f, 26f, h - 16f, cornerPaint)
        canvas.drawLine(w - 26f, h - 16f, w - 16f, h - 16f, cornerPaint)
        canvas.drawLine(w - 16f, h - 26f, w - 16f, h - 16f, cornerPaint)

        bm
    }

    fun getRoyalEmerald(w: Int = 380, h = 380): Bitmap = cache.getOrPut("royal_emerald") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.5f, h * 0.4f, w * 0.7f,
                intArrayOf(0xFF064E3B.toInt(), 0xFF043427.toInt(), 0xFF021E16.toInt()),
                floatArrayOf(0f, 0.6f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val goldRim1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            color = 0xFFF59E0B.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, goldRim1)

        val innerRect = RectF(14f, 14f, w - 14f, h - 14f)
        val goldRim2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            color = 0x66FDE68A.toInt()
        }
        canvas.drawRoundRect(innerRect, rad - 8f, rad - 8f, goldRim2)
        bm
    }

    fun getExecutiveLeather(w: Int = 640, h = 180): Bitmap = cache.getOrPut("executive_leather") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF191614.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val bronzeRim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = 0xFFD97706.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, bronzeRim)

        val stitchPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.8f
            color = 0x77F59E0B.toInt()
        }
        val innerRect = RectF(13f, 13f, w - 13f, h - 13f)
        canvas.drawRoundRect(innerRect, rad - 7f, rad - 7f, stitchPaint)
        bm
    }

    fun getAnalogCelestial(w: Int = 640, h = 320): Bitmap = cache.getOrPut("analog_celestial") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(w * 0.5f, h * 0.35f, w * 0.8f,
                intArrayOf(0xFF1E293B.toInt(), 0xFF0F172A.toInt(), 0xFF070B14.toInt()),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val goldRim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            color = 0xFFF59E0B.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, goldRim)
        bm
    }

    fun getGlassCelestial(isDay: Boolean = true, w: Int = 640, h = 180): Bitmap = cache.getOrPut("glass_celestial_$isDay") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val colors = if (isDay) {
            intArrayOf(0xE01E293B.toInt(), 0xD00F172A.toInt(), 0xC00284C7.toInt())
        } else {
            intArrayOf(0xE00F172A.toInt(), 0xD01E1B4B.toInt(), 0xC0312E81.toInt())
        }
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(), colors, floatArrayOf(0f, 0.6f, 1f), Shader.TileMode.CLAMP)
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = if (isDay) 0x6638BDF8.toInt() else 0x66818CF8.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getModernCapsule(w: Int = 640, h = 180): Bitmap = cache.getOrPut("modern_capsule") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), 0f,
                intArrayOf(0xD50F172A.toInt(), 0xCC1E293B.toInt(), 0xD50F172A.toInt()),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0x5538BDF8.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getCompactClassic(w: Int = 640, h = 180): Bitmap = cache.getOrPut("compact_classic") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, 0f, h.toFloat(),
                intArrayOf(0xF01E1B4B.toInt(), 0xF00F172A.toInt()),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0x666366F1.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getMinimalPill(w: Int = 640, h = 180): Bitmap = cache.getOrPut("minimal_pill") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(0xE0064E3B.toInt(), 0xD0022C22.toInt()),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0x6634D399.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getSplitHorizon(w: Int = 640, h = 180): Bitmap = cache.getOrPut("split_horizon") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xF00F172A.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = 0xFFF59E0B.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getPaperClean(w: Int = 640, h = 180): Bitmap = cache.getOrPut("paper_clean") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF8FAFC.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0xFFCBD5E1.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getRetroDigital(w: Int = 380, h = 380): Bitmap = cache.getOrPut("retro_digital") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF8B9D83.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        // Pixel scanlines
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x221E2819.toInt()
            strokeWidth = 1f
        }
        var y = 14f
        while (y < h - 14f) {
            canvas.drawLine(14f, y, w - 14f, y, linePaint)
            y += 8f
        }

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = 0xFF283622.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getCalendarTile(w: Int = 380, h = 380): Bitmap = cache.getOrPut("calendar_tile") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 44f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF1E293B.toInt() }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = 0xFFF59E0B.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }

    fun getTitaniumChronos(w: Int = 640, h = 180): Bitmap = cache.getOrPut("titanium_chronos") {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        val rect = RectF(6f, 6f, w - 6f, h - 6f)
        val rad = 38f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(0xFF1E232A.toInt(), 0xFF28303C.toInt(), 0xFF181C22.toInt()),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, rad, rad, bgPaint)

        val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = 0xFF94A3B8.toInt()
        }
        canvas.drawRoundRect(rect, rad, rad, rim)
        bm
    }
}

/** ۱. متن ساده بدون پس‌زمینه (شفاف) **/
class PureTextWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, PureTextWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                val fullText = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}"
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .then(tapModifier)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🕒  ${jalaliDate.timePersianSpaced}",
                            style = TextStyle(color = ColorProvider(Color(0xEEFFFFFF)), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(3.dp))
                        Text(
                            text = fullText,
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(3.dp))
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0xCCFFFFFF)), fontSize = 12.sp, fontWeight = FontWeight.Normal)
                        )
                    }
                }
            }
        }
    }
}

/** ۲. شیشه‌ای آسمانی خورشید و ماه **/
class GlassCelestialWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, GlassCelestialWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getGlassCelestial(jalaliDate.isDaytime)

        provideContent {
            GlanceTheme {
                val icon = if (jalaliDate.isDaytime) "☀️" else "🌙"
                val periodLabel = if (jalaliDate.isDaytime) "روز • ${jalaliDate.dayOfWeekSimple}" else "شب • ${jalaliDate.dayOfWeekSimple}"
                val periodColor = if (jalaliDate.isDaytime) Color(0xFFFDE68A) else Color(0xFFC7D2FE)

                Box(
                    modifier = GlanceModifier.fillMaxSize().then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(
                        modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = GlanceModifier.width(44.dp).height(44.dp).cornerRadius(14.dp).background(if (jalaliDate.isDaytime) Color(0xFFF59E0B) else Color(0xFF312E81)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = icon, style = TextStyle(fontSize = 22.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = periodLabel, style = TextStyle(color = ColorProvider(periodColor), fontSize = 12.sp, fontWeight = FontWeight.Bold))
                            Spacer(modifier = GlanceModifier.height(2.dp))
                            Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 17.sp, fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۳. کپسول شیشه‌ای مدرن **/
class ModernCapsuleWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, ModernCapsuleWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getModernCapsule()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                            Text(text = "سال ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xCCFFFFFF)), fontSize = 11.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = jalaliDate.dayOfWeekSimple, style = TextStyle(color = ColorProvider(Color(0xFF7DD3FC)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۴. کلاسیک فشرده تقویم و ساعت **/
class CompactClassicWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, CompactClassicWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getCompactClassic()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF818CF8)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۵. کپسول مینیمال **/
class MinimalPillWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, MinimalPillWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getMinimalPill()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌿 ${jalaliDate.dayOfWeekSimple}", style = TextStyle(color = ColorProvider(Color(0xFF6EE7B7)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۶. افق دوتکه **/
class SplitHorizonWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, SplitHorizonWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getSplitHorizon()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۷. کاغذ روشن **/
class PaperCleanWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, PaperCleanWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getPaperClean()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📅 ${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color(0xFF0F172A)), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = jalaliDate.timePersian, style = TextStyle(color = ColorProvider(Color(0xFF0284C7)), fontSize = 13.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۸. ساعت دیجیتال رترو LCD **/
class RetroDigitalWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, RetroDigitalWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getRetroDigital()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = jalaliDate.timePersianSpaced, style = TextStyle(color = ColorProvider(Color(0xFF1E2819)), fontSize = 28.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color(0xFF283622)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۹. کارت شیشه‌ای نئون فیروزه‌ای با هاله نور واقعی (۱×۳) **/
class NeonCyanCardWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, NeonCyanCardWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getNeonCyan()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = GlanceModifier.cornerRadius(12.dp).background(Color(0x3500F0FF)).padding(horizontal = 9.dp, vertical = 5.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "⚡ ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                                Text(text = jalaliDate.gregorianDateString, style = TextStyle(color = ColorProvider(Color(0xFF67E8F9)), fontSize = 8.sp))
                            }
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "CYBER ⚡ ${jalaliDate.dayOfWeekSimple}", style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 11.sp, fontWeight = FontWeight.Bold))
                            Text(text = "سال ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFFE0F2FE)), fontSize = 11.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Box(modifier = GlanceModifier.cornerRadius(12.dp).background(Color(0x50003049)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

/** ۱۰. کاشی تقویم دیواری روز درشت (۲×۲) **/
class CalendarTileWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, CalendarTileWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getCalendarTile()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = jalaliDate.dayOfWeekSimple, style = TextStyle(color = ColorProvider(Color(0xFFFEF3C7)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text(text = jalaliDate.dayPersian, style = TextStyle(color = ColorProvider(Color.White), fontSize = 42.sp, fontWeight = FontWeight.Bold))
                        Text(text = "${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF94A3B8)), fontSize = 11.sp))
                    }
                }
            }
        }
    }
}

/** ۱۱. آبسیدین متالیک با طلای ۲۴ عیار (۱×۳ Pro) **/
class LuxuryDarkWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, LuxuryDarkWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getLuxuryDark()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "👑 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                            Text(text = "✦ ${jalaliDate.gregorianDateString}", style = TextStyle(color = ColorProvider(Color(0xFFD4AF37)), fontSize = 9.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "PRO LUXURY • " + jalaliDate.dayOfWeekSimple, style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 10.sp, fontWeight = FontWeight.Bold))
                            Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

/** ۱۲. شیشه آئورا با بازتاب کریستالی (۱×۴ Pro) **/
class AuroraGlassWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, AuroraGlassWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getAuroraGlass()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌌 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF67E8F9)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = jalaliDate.gregorianDateString, style = TextStyle(color = ColorProvider(Color(0xFFE9D5FF)), fontSize = 10.sp))
                    }
                }
            }
        }
    }
}

/** ۱۳. داشبورد سایبرگلد مربعی (۲×۲ Pro) **/
class CyberGold2x2Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, CyberGold2x2WidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getCyberGold2x2()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "☀️ ${jalaliDate.timePersianSpaced}", style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 24.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(text = jalaliDate.dayOfWeekSimple, style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Text(text = jalaliDate.gregorianDateString, style = TextStyle(color = ColorProvider(Color(0xAAFFFFFF)), fontSize = 10.sp))
                    }
                }
            }
        }
    }
}

/** ۱۴. منشور الماسی ۳بعدی با شکست نور (۱×۳ Pro) **/
class DiamondPrismWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, DiamondPrismWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getDiamondPrism()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💎 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFBAE6FD)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = jalaliDate.dayOfWeekSimple, style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            Text(text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFF7DD3FC)), fontSize = 13.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

/** ۱۵. چرم مشکی دست‌دوز با پلاک برنز (۱×۴ Pro) **/
class ExecutiveLeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, ExecutiveLeatherWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getExecutiveLeather()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚜️ ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFFF5F5F4)), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = jalaliDate.gregorianDateString, style = TextStyle(color = ColorProvider(Color(0xFFA8A29E)), fontSize = 10.sp))
                    }
                }
            }
        }
    }
}

/** ۱۶. مگا داشبورد ۲×۴ سلطنتی با فاز ماه (۲×۴ Pro) **/
class AnalogCelestialWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, AnalogCelestialWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getAnalogCelestial()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👑 داشبورد سلطنتی تقویم و زمان", style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 12.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 19.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(text = "ساعت: ${jalaliDate.timePersianSpaced} • ${jalaliDate.gregorianDateString}", style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 13.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

/** ۱۷. هولوگرام شناور سایبرپانک با خطوط اسکن (۱×۳ Pro) **/
class HologramGridWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, HologramGridWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getHologramGrid()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = GlanceModifier.cornerRadius(10.dp).background(Color(0x3500F0FF)).padding(horizontal = 9.dp, vertical = 5.dp)) {
                            Text(text = "🌐 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            Text(text = "HOLO MATRIX • ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFF67E8F9)), fontSize = 10.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Box(modifier = GlanceModifier.cornerRadius(10.dp).background(Color(0x400284C7)).padding(horizontal = 8.dp, vertical = 5.dp)) {
                            Text(text = "HOLO", style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 11.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

/** ۱۸. زمرد سلطنتی و برنج صیقلی فاخر (۲×۲ Pro) **/
class RoyalEmeraldWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, RoyalEmeraldWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getRoyalEmerald()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚜️ ${jalaliDate.dayOfWeekSimple}", style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Text(text = jalaliDate.dayPersian, style = TextStyle(color = ColorProvider(Color.White), fontSize = 38.sp, fontWeight = FontWeight.Bold))
                        Text(text = "${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFF6EE7B7)), fontSize = 13.sp, fontWeight = FontWeight.Bold))
                        Text(text = "🕒 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 11.sp))
                    }
                }
            }
        }
    }
}

/** ۱۹. شیشه مایع ژلاتینی مات با حباب‌های ۳بعدی و انعکاس نور (۱×۳ Pro) **/
class FrostedLiquidWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, FrostedLiquidWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getFrostedLiquid()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = GlanceModifier.cornerRadius(12.dp).background(Color(0x35FFFFFF)).padding(horizontal = 9.dp, vertical = 5.dp)) {
                            Text(text = "🫧 ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFFFBCFE8)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold))
                            Text(text = "بلور مایع • ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color(0xFFBAE6FD)), fontSize = 10.sp))
                        }
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Box(modifier = GlanceModifier.cornerRadius(12.dp).background(Color(0x4038BDF8)).padding(horizontal = 9.dp, vertical = 5.dp)) {
                            Text(text = "💧 مایع", style = TextStyle(color = ColorProvider(Color.White), fontSize = 11.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

/** ۲۰. تیتانیوم فضاپیمایی با زمان‌سنج دقیق (۱×۴ Pro) **/
class TitaniumChronosWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, TitaniumChronosWidgetReceiver::class.java)
        val bg = WidgetBackgroundFactory.getTitaniumChronos()

        provideContent {
            GlanceTheme {
                Box(modifier = GlanceModifier.fillMaxSize().then(tapModifier), contentAlignment = Alignment.Center) {
                    Image(provider = ImageProvider(bg), contentDescription = null, modifier = GlanceModifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                    Row(modifier = GlanceModifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⏱️ ${jalaliDate.timePersian}", style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}", style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(text = jalaliDate.gregorianDateString, style = TextStyle(color = ColorProvider(Color(0xFF94A3B8)), fontSize = 11.sp))
                    }
                }
            }
        }
    }
}
