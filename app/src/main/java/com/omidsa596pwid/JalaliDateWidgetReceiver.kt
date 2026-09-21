package com.omidsa596pwid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Helper object to update all 20 widget styles simultaneously
object AllWidgetsUpdater {
    suspend fun updateAllVariants(context: Context) {
        try { PureTextWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating PureTextWidget", e) }
        try { GlassCelestialWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating GlassCelestialWidget", e) }
        try { ModernCapsuleWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating ModernCapsuleWidget", e) }
        try { CompactClassicWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating CompactClassicWidget", e) }
        try { MinimalPillWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating MinimalPillWidget", e) }
        try { SplitHorizonWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating SplitHorizonWidget", e) }
        try { PaperCleanWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating PaperCleanWidget", e) }
        try { RetroDigitalWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating RetroDigitalWidget", e) }
        try { NeonCyanCardWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating NeonCyanCardWidget", e) }
        try { CalendarTileWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating CalendarTileWidget", e) }
        try { LuxuryDarkWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating LuxuryDarkWidget", e) }
        try { AuroraGlassWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating AuroraGlassWidget", e) }
        try { CyberGold2x2Widget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating CyberGold2x2Widget", e) }
        try { DiamondPrismWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating DiamondPrismWidget", e) }
        try { ExecutiveLeatherWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating ExecutiveLeatherWidget", e) }
        try { AnalogCelestialWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating AnalogCelestialWidget", e) }
        try { HologramGridWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating HologramGridWidget", e) }
        try { RoyalEmeraldWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating RoyalEmeraldWidget", e) }
        try { FrostedLiquidWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating FrostedLiquidWidget", e) }
        try { TitaniumChronosWidget().updateAll(context) } catch (e: Exception) { Log.e("JalaliWidget", "Error updating TitaniumChronosWidget", e) }
    }
}

// ۱. متن ساده بدون پس‌زمینه (شفاف) Receiver
class PureTextWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PureTextWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۲. شیشه‌ای ۲۰٪ با خورشید و ماه Receiver
class GlassCelestialWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GlassCelestialWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۳. کپسول شیشه‌ای مدرن Receiver
class ModernCapsuleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ModernCapsuleWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۴. کلاسیک تقویمی ساده فشرده Receiver
class CompactClassicWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CompactClassicWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۵. کپسول مینیاتوری مینیمال Receiver
class MinimalPillWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MinimalPillWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۶. افقی دو تکه باریک با جداکننده Receiver
class SplitHorizonWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SplitHorizonWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۷. طرح کاغذ مینیمال نوردیک Receiver
class PaperCleanWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PaperCleanWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۸. دیجیتال ال‌سی‌دی نوستالژیک Receiver
class RetroDigitalWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RetroDigitalWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۹. کارت شیشه‌ای نئون فیروزه‌ای Receiver
class NeonCyanCardWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NeonCyanCardWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۰. کاشی تقویم دیواری روز درشت Receiver
class CalendarTileWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CalendarTileWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۱. آبسیدین متالیک با طلای ۲۴ عیار Receiver
class LuxuryDarkWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LuxuryDarkWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۲. شیشه آئورا با بازتاب کریستالی Receiver
class AuroraGlassWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AuroraGlassWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۳. داشبورد سایبرگلد مربعی (ساعت و تقویم) Receiver
class CyberGold2x2WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CyberGold2x2Widget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۴. منشور الماسی ۳بعدی با شکست نور Receiver
class DiamondPrismWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DiamondPrismWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۵. چرم مشکی دست‌دوز با پلاک برنز Receiver
class ExecutiveLeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ExecutiveLeatherWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۶. مگا داشبورد ۲×۴ سلطنتی با فاز ماه Receiver
class AnalogCelestialWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AnalogCelestialWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۷. هولوگرام شناور سایبرپانک Receiver
class HologramGridWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HologramGridWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۸. زمرد سلطنتی و برنج صیقلی فاخر Receiver
class RoyalEmeraldWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RoyalEmeraldWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۱۹. شیشه مایع ژلاتینی مات Receiver
class FrostedLiquidWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FrostedLiquidWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

// ۲۰. تیتانیوم فضاپیمایی با زمان‌سنج دقیق Receiver
class TitaniumChronosWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TitaniumChronosWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        handleCommonActions(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
        MidnightScheduler.scheduleAll(context)
    }
}

private fun handleCommonActions(context: Context, intent: Intent) {
    val action = intent.action ?: return
    Log.d("JalaliWidget", "Received action: $action")

    when (action) {
        "com.omidsa596pwid.ACTION_MIDNIGHT_UPDATE",
        "com.omidsa596pwid.ACTION_MANUAL_REFRESH",
        Intent.ACTION_DATE_CHANGED,
        Intent.ACTION_TIMEZONE_CHANGED,
        "android.intent.action.TIME_SET",
        Intent.ACTION_BOOT_COMPLETED,
        "android.intent.action.QUICKBOOT_POWERON" -> {
            refreshAll(context)
            MidnightScheduler.scheduleAll(context)
        }
        Intent.ACTION_USER_PRESENT,
        Intent.ACTION_SCREEN_ON -> {
            val prefs = context.getSharedPreferences("jalali_widget_state", Context.MODE_PRIVATE)
            val lastDay = prefs.getInt("last_rendered_day", -1)
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            if (lastDay != currentDay) {
                Log.d("JalaliWidget", "Date rollover detected on screen on ($lastDay -> $currentDay). Refreshing widgets!")
                refreshAll(context)
                MidnightScheduler.scheduleAll(context)
            }
        }
    }
}

private fun refreshAll(context: Context) {
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    context.getSharedPreferences("jalali_widget_state", Context.MODE_PRIVATE)
        .edit()
        .putInt("last_rendered_day", currentDay)
        .apply()

    CoroutineScope(Dispatchers.IO).launch {
        AllWidgetsUpdater.updateAllVariants(context)
    }
}

/**
 * Worker invoked at midnight or by periodic safety net
 */
class MidnightWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("JalaliWidget", "MidnightWorker running...")
        try {
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            context.getSharedPreferences("jalali_widget_state", Context.MODE_PRIVATE)
                .edit()
                .putInt("last_rendered_day", currentDay)
                .apply()

            AllWidgetsUpdater.updateAllVariants(context)
            MidnightScheduler.scheduleAll(context)
        } catch (e: Exception) {
            Log.e("JalaliWidget", "MidnightWorker error", e)
        }
        return Result.success()
    }
}

/**
 * Multi-layer Scheduler:
 * 1. AlarmManager.setExactAndAllowWhileIdle (RTC_WAKEUP at 00:00:02)
 * 2. WorkManager OneTimeWork (with initial delay until 00:00:05)
 * 3. WorkManager Periodic Backup (every 1 hour fallback)
 */
object MidnightScheduler {
    const val ACTION_MIDNIGHT_UPDATE = "com.omidsa596pwid.ACTION_MIDNIGHT_UPDATE"
    const val ACTION_MANUAL_REFRESH = "com.omidsa596pwid.ACTION_MANUAL_REFRESH"

    fun scheduleAll(context: Context) {
        scheduleAlarmManager(context)
        scheduleWorkManager(context)
    }

    private fun scheduleAlarmManager(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, PureTextWidgetReceiver::class.java).apply {
                action = ACTION_MIDNIGHT_UPDATE
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1001,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            // Exact midnight trigger (00:00:02)
            val midnight = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 2)
                set(Calendar.MILLISECOND, 0)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    midnight.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    midnight.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("JalaliWidget", "AlarmManager scheduled for ${midnight.time}")
        } catch (e: Exception) {
            Log.e("JalaliWidget", "Error scheduling AlarmManager", e)
        }
    }

    private fun scheduleWorkManager(context: Context) {
        try {
            val now = System.currentTimeMillis()
            val nextMidnight = Calendar.getInstance().apply {
                timeInMillis = now
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 5)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val delayMillis = (nextMidnight - now).coerceAtLeast(1000L)

            // 1. Precise OneTimeWork around midnight
            val midnightWork = OneTimeWorkRequestBuilder<MidnightWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "MidnightWidgetRefresh",
                ExistingWorkPolicy.REPLACE,
                midnightWork
            )

            // 2. Periodic hourly backup safety net
            val periodicWork = PeriodicWorkRequestBuilder<MidnightWorker>(1, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "HourlyWidgetSafetyNet",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork
            )
            Log.d("JalaliWidget", "WorkManager midnight task enqueued (delay: ${delayMillis / 1000}s)")
        } catch (e: Exception) {
            Log.e("JalaliWidget", "Error scheduling WorkManager", e)
        }
    }
}
