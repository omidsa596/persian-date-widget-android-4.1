package com.omidsa596pwid

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
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
                            style = TextStyle(
                                color = ColorProvider(Color(0xEEFFFFFF)),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = GlanceModifier.height(3.dp))
                        Text(
                            text = fullText,
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = GlanceModifier.height(3.dp))
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(
                                color = ColorProvider(Color(0xCCFFFFFF)),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
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
        val isRtl = context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

        provideContent {
            GlanceTheme {
                val icon = if (jalaliDate.isDaytime) "☀️" else "🌙"
                val periodLabel = if (jalaliDate.isDaytime) "روز • ${jalaliDate.dayOfWeekSimple}" else "شب • ${jalaliDate.dayOfWeekSimple}"
                val periodColor = if (jalaliDate.isDaytime) Color(0xFFFDE68A) else Color(0xFFC7D2FE)

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(22.dp)
                        .background(Color(0x35FFFFFF))
                        .padding(1.dp)
                        .then(tapModifier)
                ) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .cornerRadius(21.dp)
                            .background(Color(0x28000000))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isRtl) {
                                // Right in RTL: Sun/Moon
                                Box(
                                    modifier = GlanceModifier
                                        .width(44.dp)
                                        .height(44.dp)
                                        .cornerRadius(14.dp)
                                        .background(if (jalaliDate.isDaytime) Color(0xFFF59E0B) else Color(0xFF312E81)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = icon, style = TextStyle(fontSize = 22.sp))
                                }

                                Spacer(modifier = GlanceModifier.defaultWeight())

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = periodLabel,
                                        style = TextStyle(color = ColorProvider(periodColor), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = GlanceModifier.height(2.dp))
                                    Text(
                                        text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                                        style = TextStyle(color = ColorProvider(Color.White), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    )
                                }

                                Spacer(modifier = GlanceModifier.defaultWeight())

                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "🕒 ${jalaliDate.timePersian}",
                                        style = TextStyle(color = ColorProvider(Color(0xEEFFFFFF)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = jalaliDate.gregorianDateString,
                                        style = TextStyle(color = ColorProvider(Color(0x99FFFFFF)), fontSize = 9.sp)
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "🕒 ${jalaliDate.timePersian}",
                                        style = TextStyle(color = ColorProvider(Color(0xEEFFFFFF)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = jalaliDate.gregorianDateString,
                                        style = TextStyle(color = ColorProvider(Color(0x99FFFFFF)), fontSize = 9.sp)
                                    )
                                }

                                Spacer(modifier = GlanceModifier.defaultWeight())

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = periodLabel,
                                        style = TextStyle(color = ColorProvider(periodColor), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = GlanceModifier.height(2.dp))
                                    Text(
                                        text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                                        style = TextStyle(color = ColorProvider(Color.White), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    )
                                }

                                Spacer(modifier = GlanceModifier.defaultWeight())

                                Box(
                                    modifier = GlanceModifier
                                        .width(44.dp)
                                        .height(44.dp)
                                        .cornerRadius(14.dp)
                                        .background(if (jalaliDate.isDaytime) Color(0xFFF59E0B) else Color(0xFF312E81)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = icon, style = TextStyle(fontSize = 22.sp))
                                }
                            }
                        }
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(32.dp)
                        .background(Color(0x35FFFFFF))
                        .padding(1.dp)
                        .then(tapModifier)
                ) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .cornerRadius(31.dp)
                            .background(Color(0x2B0B132B))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🕒 ${jalaliDate.timePersian}",
                                style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "سال ${jalaliDate.yearPersian}",
                                    style = TextStyle(color = ColorProvider(Color(0xCCFFFFFF)), fontSize = 11.sp)
                                )
                            }
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Box(
                                modifier = GlanceModifier
                                    .cornerRadius(16.dp)
                                    .background(Color(0x660284C7))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "📅 ${jalaliDate.dayOfWeekSimple}",
                                    style = TextStyle(color = ColorProvider(Color(0xFFE0F2FE)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** ۴. کلاسیک تقویمی ساده فشرده (۱×۲) **/
class CompactClassicWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, CompactClassicWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(16.dp)
                        .background(Color(0x35000000))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.dayOfWeekSimple,
                            style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

/** ۵. کپسول مینیاتوری مینیمال (۱×۲) **/
class MinimalPillWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, MinimalPillWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(24.dp)
                        .background(Color(0x401E293B))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = jalaliDate.dayPersian,
                            style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.width(6.dp))
                        Text(
                            text = jalaliDate.monthName,
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

/** ۶. افقی دو تکه باریک با جداکننده (۱×۴) **/
class SplitHorizonWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, SplitHorizonWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(18.dp)
                        .background(Color(0x350F172A))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0xAAFFFFFF)), fontSize = 11.sp)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple}  •  ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "🕒 ${jalaliDate.timePersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

/** ۷. طرح کاغذ مینیمال نوردیک (۱×۳) **/
class PaperCleanWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, PaperCleanWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(18.dp)
                        .background(Color(0xF0F8FAFC))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF0F172A)), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(2.dp))
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0xFF64748B)), fontSize = 11.sp)
                        )
                    }
                }
            }
        }
    }
}

/** ۸. دیجیتال ال‌سی‌دی نوستالژیک (۲×۲) **/
class RetroDigitalWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, RetroDigitalWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(18.dp)
                        .background(Color(0xFF8B9D83))
                        .padding(12.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.timePersianSpaced,
                            style = TextStyle(color = ColorProvider(Color(0xFF1E2819)), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(
                            text = jalaliDate.dayOfWeekSimple,
                            style = TextStyle(color = ColorProvider(Color(0xFF283622)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF1E2819)), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

/** ۹. کارت شیشه‌ای نئون فیروزه‌ای (۱×۳) **/
class NeonCyanCardWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, NeonCyanCardWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xFF00F0FF))
                        .padding(1.dp)
                        .then(tapModifier)
                ) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .cornerRadius(19.dp)
                            .background(Color(0xF008131E))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚡ ${jalaliDate.timePersian}",
                                style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Text(
                                text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                                style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(22.dp)
                        .background(Color(0xFF1E293B))
                        .padding(10.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.dayOfWeekSimple,
                            style = TextStyle(color = ColorProvider(Color(0xFFF59E0B)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = jalaliDate.dayPersian,
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 38.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF94A3B8)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xFFF59E0B))
                        .padding(1.5.dp)
                        .then(tapModifier)
                ) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .cornerRadius(18.5.dp)
                            .background(Color(0xF610141D))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✦ ${jalaliDate.timePersianSpaced}",
                                style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = jalaliDate.dayOfWeekSimple,
                                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                                    style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                )
                            }
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xF018132B))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ ${jalaliDate.timePersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFFC084FC)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 11.sp)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(24.dp)
                        .background(Color(0xFF161922))
                        .padding(12.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.timePersianSpaced,
                            style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(
                            text = jalaliDate.dayOfWeekSimple,
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0x88FFFFFF)), fontSize = 10.sp)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xFF38BDF8))
                        .padding(1.dp)
                        .then(tapModifier)
                ) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .cornerRadius(19.dp)
                            .background(Color(0xF00A192F))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💎 ${jalaliDate.timePersian}",
                                style = TextStyle(color = ColorProvider(Color(0xFF7DD3FC)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Text(
                                text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                                style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xFF181513))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = jalaliDate.timePersian,
                            style = TextStyle(color = ColorProvider(Color(0xFFD97706)), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFFF5F5F4)), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(26.dp)
                        .background(Color(0xF50D1322))
                        .padding(14.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "👑 داشبورد سلطنتی تقویم و زمان",
                            style = TextStyle(color = ColorProvider(Color(0xFFFBBF24)), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ساعت: ${jalaliDate.timePersianSpaced}",
                                style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = GlanceModifier.width(16.dp))
                            Text(
                                text = jalaliDate.gregorianDateString,
                                style = TextStyle(color = ColorProvider(Color(0xAAFFFFFF)), fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/** ۱۷. هولوگرام شناور سایبرپانک (۱×۳ Pro) **/
class HologramGridWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, HologramGridWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xF0051624))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 ${jalaliDate.timePersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF00F0FF)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(22.dp)
                        .background(Color(0xFF064E3B))
                        .padding(12.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.dayOfWeekSimple,
                            style = TextStyle(color = ColorProvider(Color(0xFFFDE68A)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = jalaliDate.dayPersian,
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF6EE7B7)), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

/** ۱۹. شیشه مایع ژلاتینی مات (۱×۳ Pro) **/
class FrostedLiquidWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val jalaliDate = JalaliHelper.getNow()
        val tapModifier = createRefreshModifier(context, FrostedLiquidWidgetReceiver::class.java)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(24.dp)
                        .background(Color(0x40FFFFFF))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🫧 ${jalaliDate.timePersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFFFBCFE8)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
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

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                        .background(Color(0xFF1E232A))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .then(tapModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱️ ${jalaliDate.timePersian}",
                            style = TextStyle(color = ColorProvider(Color(0xFF38BDF8)), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = "${jalaliDate.dayOfWeekSimple} ${jalaliDate.dayPersian} ${jalaliDate.monthName} ${jalaliDate.yearPersian}",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = jalaliDate.gregorianDateString,
                            style = TextStyle(color = ColorProvider(Color(0xFF94A3B8)), fontSize = 11.sp)
                        )
                    }
                }
            }
        }
    }
}
