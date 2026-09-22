package com.omidsa596pwid

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize midnight triggers and backup workers
        MidnightScheduler.scheduleAll(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0A0F1D)
                ) {
                    WidgetHubScreen(
                        developerName = "omidsa596",
                        appName = "ویجت تاریخ شمسی",
                        packageNameText = "com.omidsa596pwid",
                        versionName = "1.0.4",
                        versionCode = 4,
                        isBatteryOptimized = isIgnoringBatteryOptimizations(),
                        onPinWidget = { receiverClass -> requestPinWidget(receiverClass) },
                        onOpenBatterySettings = { requestIgnoreBatteryOptimization() }
                    )
                }
            }
        }
    }

    private fun isIgnoringBatteryOptimizations(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = getSystemService(Context.POWER_SERVICE) as? PowerManager
            pm?.isIgnoringBatteryOptimizations(packageName) ?: false
        } else {
            true
        }
    }

    @SuppressLint("BatteryLife")
    private fun requestIgnoreBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            } catch (e: Exception) {
                try {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    startActivity(intent)
                } catch (e2: Exception) {
                    Toast.makeText(this, "لطفاً در تنظیمات باتری گوشی، بهینه‌سازی را برای این برنامه غیرفعال کنید", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "در این نسخه اندروید نیازی به تنظیمات باتری نیست", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPinWidget(receiverClass: Class<*>) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appWidgetManager = getSystemService(AppWidgetManager::class.java)
                val myProvider = ComponentName(this, receiverClass)
                if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported) {
                    appWidgetManager.requestPinAppWidget(myProvider, null, null)
                    Toast.makeText(this, "ویجت برای افزودن به هوم‌اسکرین پیشنهاد شد", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            Toast.makeText(this, "روی صفحه اصلی گوشی لمس طولانی کرده و ویجت مورد نظر را اضافه کنید", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "روی صفحه اصلی لمس طولانی کرده و ویجت را انتخاب کنید", Toast.LENGTH_LONG).show()
        }
    }
}

data class WidgetUiOption(
    val id: String,
    val title: String,
    val desc: String,
    val sizeBadge: String,
    val tier: String,
    val tierBadge: String,
    val receiverClass: Class<*>
)

@Composable
fun WidgetHubScreen(
    developerName: String,
    appName: String,
    packageNameText: String,
    versionName: String,
    versionCode: Int,
    isBatteryOptimized: Boolean,
    onPinWidget: (Class<*>) -> Unit,
    onOpenBatterySettings: () -> Unit
) {
    val jalaliDate = remember { JalaliHelper.getNow() }
    var selectedFilter by remember { mutableStateOf("all") } // "all", "free", "premium"

    val allWidgets = remember {
        listOf(
            WidgetUiOption(
                id = "pure_text",
                title = "۱. متن ساده بدون پس‌زمینه (شفاف)",
                desc = "تک‌خطی و کاملاً شفاف؛ فونت خوانا بدون کادر، مناسب قرارگیری مستقیم روی والپیپر",
                sizeBadge = "۱×۳",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = PureTextWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "glass_celestial",
                title = "۲. شیشه‌ای ۲۰٪ با خورشید و ماه",
                desc = "کارت شیشه‌ای ۲۰٪ ترنسپرنت؛ در روز آیکون خورشید ☀️ و در شب آیکون ماه 🌙",
                sizeBadge = "۱×۳",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = GlassCelestialWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "modern_capsule",
                title = "۳. کپسول شیشه‌ای مدرن",
                desc = "کپسول گرد بلوری؛ تفکیک روز هفته با نشان آبی‌رنگ و ساعت زنده",
                sizeBadge = "۱×۳",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = ModernCapsuleWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "compact_classic",
                title = "۴. کلاسیک تقویمی ساده فشرده",
                desc = "ابعاد جمع‌وجور ۱×۲ با نمایش تقویمی متمرکز و تاریخ شمسی خوانا",
                sizeBadge = "۱×۲",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = CompactClassicWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "minimal_pill",
                title = "۵. کپسول مینیاتوری مینیمال",
                desc = "پیل گرد ظریف با تاریخ شمسی مختصر و ساعت فشرده برای فضاهای محدود",
                sizeBadge = "۱×۲",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = MinimalPillWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "split_horizon",
                title = "۶. افقی دو تکه باریک با جداکننده",
                desc = "کارت عریض ۱×۴ با جداکننده شیشه‌ای و تفکیک متقارن زمان و تقویم شمسی",
                sizeBadge = "۱×۴",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = SplitHorizonWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "paper_clean",
                title = "۷. طرح کاغذ مینیمال نوردیک",
                desc = "پس‌زمینه سفید مات با فونت مشکی دودی بسیار خوانا و کنتراست بالا",
                sizeBadge = "۱×۳",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = PaperCleanWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "retro_digital",
                title = "۸. دیجیتال ال‌سی‌دی نوستالژیک",
                desc = "طراحی رترو نمایشگر ال‌سی‌دی با تاریخ شمسی تقویمی و ساعت بزرگ",
                sizeBadge = "۲×۲",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = RetroDigitalWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "neon_cyan_card",
                title = "۹. کارت شیشه‌ای نئون فیروزه‌ای",
                desc = "هاله نور فیروزه‌ای سیان با استایل سایبرپانک و تقویم درخشان",
                sizeBadge = "۱×۳",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = NeonCyanCardWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "calendar_tile",
                title = "۱۰. کاشی تقویم دیواری روز درشت",
                desc = "کاشی ۲×۲ متمرکز با عدد روز بزرگ، نام ماه و سال شمسی",
                sizeBadge = "۲×۲",
                tier = "free",
                tierBadge = "رایگان",
                receiverClass = CalendarTileWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "luxury_dark",
                title = "۱۱. آبسیدین متالیک با طلای ۲۴ عیار",
                desc = "شیشه تیره دودی با خطوط و فونت متالیک طلایی باشکوه و کادر تقویم اختصاصی",
                sizeBadge = "۱×۳",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = LuxuryDarkWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "aurora_glass",
                title = "۱۲. شیشه آئورا با بازتاب کریستالی",
                desc = "طیف رنگین شفق قطبی با هاله بنفش-فیروزه‌ای و تقویم ۳گانه کامل",
                sizeBadge = "۱×۴",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = AuroraGlassWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "cyber_gold_2x2",
                title = "۱۳. داشبورد سایبرگلد مربعی (ساعت و تقویم)",
                desc = "داشبورد ۲×۲ لوکس با ساعت دیجیتال زنده، کادر طلایی متالیک و تاریخ کامل",
                sizeBadge = "۲×۲",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = CyberGold2x2WidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "diamond_prism",
                title = "۱۴. منشور الماسی ۳بعدی با شکست نور",
                desc = "کادر الماس‌تراش با پرتوهای نوری و فونت طلایی براق سلطنتی",
                sizeBadge = "۱×۳",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = DiamondPrismWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "executive_leather",
                title = "۱۵. چرم مشکی دست‌دوز با پلاک برنز",
                desc = "بافت چرم و فیبر کربن تیره با پلاک فلزی برنز پولیش‌خورده",
                sizeBadge = "۱×۴",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = ExecutiveLeatherWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "analog_celestial",
                title = "۱۶. مگا داشبورد ۲×۴ سلطنتی با فاز ماه",
                desc = "تقویم کامل ۳گانه (شمسی، قمری، میلادی)، فاز زنده ماه و مناسبت روز در ابعاد ۲×۴",
                sizeBadge = "۲×۴",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = AnalogCelestialWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "hologram_grid",
                title = "۱۷. هولوگرام شناور سایبرپانک",
                desc = "گرید ماتریسی لیزری آبی با افکت اسکنر راداری و تایپوگرافی هولوگرافیک",
                sizeBadge = "۱×۳",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = HologramGridWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "royal_emerald",
                title = "۱۸. زمرد سلطنتی و برنج صیقلی فاخر",
                desc = "زمرد عمیق یشمی با کادر طلاکوب و قلم فاخر ایرانی برای تقویم",
                sizeBadge = "۲×۲",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = RoyalEmeraldWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "frosted_liquid",
                title = "۱۹. شیشه مایع ژلاتینی مات",
                desc = "حباب‌های نوری شناور با افکت گلس بلور فوق‌العاده نرم و تایپوگرافی پاستلی",
                sizeBadge = "۱×۳",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = FrostedLiquidWidgetReceiver::class.java
            ),
            WidgetUiOption(
                id = "titanium_chronos",
                title = "۲۰. تیتانیوم فضاپیمایی با زمان‌سنج دقیق",
                desc = "بدنه تیتانیوم گرید هوافضا با رینگ زمان‌سنج، ثانیه‌شمار زنده و نمایش تقویم",
                sizeBadge = "۱×۴",
                tier = "premium",
                tierBadge = "پریمیوم VIP",
                receiverClass = TitaniumChronosWidgetReceiver::class.java
            )
        )
    }

    val filteredList = remember(selectedFilter, allWidgets) {
        when (selectedFilter) {
            "free" -> allWidgets.filter { it.tier == "free" }
            "premium" -> allWidgets.filter { it.tier == "premium" }
            else -> allWidgets
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Header Card
        item {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF1E2E4B), RoundedCornerShape(26.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFF0284C7)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🗓️", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = appName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "توسعه‌دهنده: " + developerName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = packageNameText,
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x3310B981)
                    ) {
                        Text(
                            text = "نسخه " + versionName + " (کد " + versionCode.toString() + ")",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF34D399),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "تمامی ۲۰ استایل ویجت (۱۰ نسخه رایگان و ۱۰ نسخه پرو) به طور کامل داخل این بسته قرار دارند. هرکدام را که می‌پسندید مستقیماً با دکمه زیر آن به هوم‌اسکرین پین کنید.",
                        fontSize = 11.5.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Filter Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTabButton(
                    label = "همه (۲۰ ویجت)",
                    isSelected = selectedFilter == "all",
                    onClick = { selectedFilter = "all" },
                    modifier = Modifier.weight(1f)
                )
                FilterTabButton(
                    label = "رایگان (۱۰)",
                    isSelected = selectedFilter == "free",
                    onClick = { selectedFilter = "free" },
                    modifier = Modifier.weight(1f)
                )
                FilterTabButton(
                    label = "پریمیوم (۱۰)",
                    isSelected = selectedFilter == "premium",
                    onClick = { selectedFilter = "premium" },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Widget Items
        items(filteredList, key = { it.id }) { widget ->
            WidgetCardItem(
                widget = widget,
                jalaliDate = jalaliDate,
                onPin = { onPinWidget(widget.receiverClass) }
            )
        }

        // Battery Optimization Footer
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B231A)),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF22C55E).copy(alpha = 0.3f), RoundedCornerShape(22.dp))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🔋", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تنظیمات بهینه‌سازی باتری و تغییر ۱۲ شب",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF86EFAC)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "برای اینکه تاریخ رأس ساعت ۱۲ شب دقیقاً ورق بخورد و سیستم‌عامل اندروید برنامه را در خواب فرو نبرد، بهینه‌سازی باتری را برای این برنامه خاموش نمایید.",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onOpenBatterySettings,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Text(
                            text = if (isBatteryOptimized) "✓ بهینه‌سازی غیرفعال است (آماده)" else "⚙️ باز کردن تنظیمات باتری این برنامه",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF0284C7) else Color(0xFF162032))
            .border(1.dp, if (isSelected) Color(0xFF38BDF8) else Color(0xFF27354D), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8)
        )
    }
}

@Composable
fun WidgetCardItem(
    widget: WidgetUiOption,
    jalaliDate: JalaliDateInfo,
    onPin: () -> Unit
) {
    val isPro = widget.tier == "premium"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF151D2E)),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isPro) Color(0xFFF59E0B).copy(alpha = 0.35f) else Color(0xFF334155),
                RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Badges & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isPro) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF0284C7).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = widget.tierBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPro) Color(0xFFFDE68A) else Color(0xFF7DD3FC),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF334155)
                    ) {
                        Text(
                            text = widget.sizeBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = widget.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = widget.desc,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 16.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Live Realistic Interactive Preview Box
            WidgetLivePreviewDispatcher(widget.id, jalaliDate)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onPin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPro) Color(0xFFD97706) else Color(0xFF0284C7)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Text(
                    text = "📌 افزودن این استایل به صفحه اصلی",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WidgetLivePreviewDispatcher(widgetId: String, jalaliDate: JalaliDateInfo) {
    when (widgetId) {
        "pure_text", "pure-text" -> PureTextLivePreview(jalaliDate)
        "glass_celestial", "glass-celestial" -> GlassCelestialLivePreview(jalaliDate)
        "modern_capsule", "modern-capsule" -> ModernCapsuleLivePreview(jalaliDate)
        "compact_classic", "compact-classic" -> CompactClassicLivePreview(jalaliDate)
        "minimal_pill", "minimal-pill" -> MinimalPillLivePreview(jalaliDate)
        "split_horizon", "split-horizon" -> SplitHorizonLivePreview(jalaliDate)
        "paper_clean", "paper-clean" -> PaperCleanLivePreview(jalaliDate)
        "retro_digital", "retro-digital" -> RetroDigitalLivePreview(jalaliDate)
        "neon_cyan_card", "neon-cyan", "neon_cyan" -> NeonCyanLivePreview(jalaliDate)
        "calendar_tile", "calendar-tile" -> CalendarTileLivePreview(jalaliDate)
        "luxury_dark", "luxury-dark" -> LuxuryDarkLivePreview(jalaliDate)
        "aurora_glass", "aurora-glass" -> AuroraGlassLivePreview(jalaliDate)
        "cyber_gold_2x2", "cyber-gold", "cyber-gold-2x2", "cyber_gold" -> CyberGoldLivePreview(jalaliDate)
        "diamond_prism", "diamond-prism" -> DiamondPrismLivePreview(jalaliDate)
        "executive_leather", "executive-leather" -> ExecutiveLeatherLivePreview(jalaliDate)
        "analog_celestial", "analog-celestial" -> AnalogCelestialLivePreview(jalaliDate)
        "hologram_grid", "hologram-grid" -> HologramGridLivePreview(jalaliDate)
        "royal_emerald", "royal-emerald" -> RoyalEmeraldLivePreview(jalaliDate)
        "frosted_liquid", "frosted-liquid" -> FrostedLiquidLivePreview(jalaliDate)
        "titanium_chronos", "titanium-chronos" -> TitaniumChronosLivePreview(jalaliDate)
        else -> LuxuryDarkLivePreview(jalaliDate)
    }
}

/** 1. Pure Text **/
@Composable
fun PureTextLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0A0F1D))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "سال " + jalaliDate.yearPersian + " • ساعت " + jalaliDate.timePersian,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = jalaliDate.gregorianDateString,
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

/** 2. Glass Celestial **/
@Composable
fun GlassCelestialLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "celestial")
    val auraAlpha by infTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "aura"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF0C1A30), Color(0xFF1E293B))
                )
            )
            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B).copy(alpha = 0.2f * auraAlpha))
                        .border(1.dp, Color(0xFFF59E0B).copy(alpha = auraAlpha), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (jalaliDate.isDaytime) "☀️" else "🌙",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = jalaliDate.dayOfWeekSimple,
                        fontSize = 11.sp,
                        color = Color(0xFFFDE68A)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x33000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7DD3FC),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/** 3. Modern Capsule **/
@Composable
fun ModernCapsuleLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0x33FFFFFF))
            .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(26.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x330284C7),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x550284C7))
            ) {
                Text(
                    text = jalaliDate.dayOfWeekSimple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7DD3FC),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Text(
                text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x44000000)
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xEEFFFFFF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/** 4. Compact Classic **/
@Composable
fun CompactClassicLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1E1B4B).copy(alpha = 0.6f))
            .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF4F46E5).copy(alpha = 0.4f))
                        .border(1.dp, Color(0xFF818CF8), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = jalaliDate.dayPersian,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = jalaliDate.monthName,
                            fontSize = 8.sp,
                            color = Color(0xFFC7D2FE)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "سال " + jalaliDate.yearPersian,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            Text(
                text = jalaliDate.timePersian,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF818CF8)
            )
        }
    }
}

/** 5. Minimal Pill **/
@Composable
fun MinimalPillLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1E293B).copy(alpha = 0.7f))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = jalaliDate.dayPersian,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFF59E0B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.monthName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = jalaliDate.dayOfWeekSimple,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x22FFFFFF)
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 6. Split Horizon **/
@Composable
fun SplitHorizonLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF042F2E).copy(alpha = 0.5f))
            .border(1.dp, Color(0xFF2DD4BF).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = jalaliDate.gregorianDateString,
                fontSize = 10.sp,
                color = Color(0xFF99F6E4)
            )
            Text(
                text = jalaliDate.dayOfWeekSimple + " • " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = jalaliDate.timePersian,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2DD4BF)
            )
        }
    }
}

/** 7. Paper Clean **/
@Composable
fun PaperCleanLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5F9))
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "سال " + jalaliDate.yearPersian,
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE2E8F0)
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/** 8. Retro Digital (Blinking LCD) **/
@Composable
fun RetroDigitalLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "lcd")
    val colonAlpha by infTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "colon"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF8B9D83))
            .border(2.dp, Color(0xFF283622), RoundedCornerShape(14.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LCD MATRIX  ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF283622)
                )
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E2819)
                )
                Text(
                    text = " :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E2819).copy(alpha = colonAlpha)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName + " " + jalaliDate.yearPersian,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF283622)
            )
        }
    }
}

/** 9. Neon Cyan (Animated Electric Pulse) **/
@Composable
fun NeonCyanLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "neon")
    val neonAlpha by infTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "neon"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF040E1A))
            .border(2.dp, Color(0xFF00F0FF).copy(alpha = neonAlpha), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00F0FF).copy(alpha = 0.2f * neonAlpha))
                        .border(1.dp, Color(0xFF00F0FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚡", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "NEON ELECTRIC • " + jalaliDate.dayOfWeekSimple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F0FF).copy(alpha = neonAlpha)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF003049).copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.6f))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00F0FF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 10. Calendar Tile **/
@Composable
fun CalendarTileLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDC2626))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = jalaliDate.dayOfWeekSimple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "📅 تقویم رومیزی",
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = jalaliDate.dayPersian,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = jalaliDate.monthName + " " + jalaliDate.yearPersian,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                }
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}

/** 11. Luxury Dark (PRO - Rotating Gold Laser Beam) **/
@Composable
fun LuxuryDarkLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "gold")
    val rotAngle by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing)),
        label = "rot"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0F172A))
            .padding(2.dp)
    ) {
        // Rotating Gold Laser Beam Canvas
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .rotate(rotAngle)
        ) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color(0xFFD97706),
                        Color(0xFFFDE047),
                        Color.White
                    )
                ),
                radius = size.maxDimension
            )
        }

        // Inner Obsidian Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF080B11))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0x44F59E0B), Color(0xFF451A03))
                                )
                            )
                            .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = jalaliDate.dayPersian,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFEF08A)
                            )
                            Text(
                                text = jalaliDate.monthName,
                                fontSize = 8.sp,
                                color = Color(0xFFFDE68A)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = jalaliDate.dayOfWeekSimple,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0x33F59E0B),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x55F59E0B))
                            ) {
                                Text(
                                    text = "PRO 👑",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE68A),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "طلای متالیک ۲۴ عیار",
                            fontSize = 9.sp,
                            color = Color(0xFFF59E0B).copy(alpha = 0.8f)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF451A03).copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "✦ " + jalaliDate.timePersian,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFDE047),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

/** 12. Aurora Glass (PRO - Shifting Plasma) **/
@Composable
fun AuroraGlassLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "aurora")
    val shiftX by infTransition.animateFloat(
        initialValue = -100f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "plasma"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF581C87), Color(0xFF064E3B), Color(0xFF831843)),
                    start = Offset(shiftX, 0f),
                    end = Offset(shiftX + 400f, 200f)
                )
            )
            .border(1.dp, Color(0xFFE879F9).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "✨", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "امواج شفق قطبی (Aurora Plasma)",
                        fontSize = 9.sp,
                        color = Color(0xFFF5D0FE)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x66000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66E879F9))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF99F6E4),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 13. Cyber Gold (PRO - Animated Rotating Sun) **/
@Composable
fun CyberGoldLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "sun")
    val sunAngle by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "sunRot"
    )
    val sunPulse by infTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "sunPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0F131D))
            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "☀️",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .rotate(sunAngle)
                            .scale(sunPulse)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFDE68A)
                    )
                    Text(
                        text = "SOLAR GOLD ☀️ • " + jalaliDate.dayOfWeekSimple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF451A03).copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 14. Diamond Prism (PRO - Diamond Shimmer) **/
@Composable
fun DiamondPrismLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "diamond")
    val shimmerX by infTransition.animateFloat(
        initialValue = -150f,
        targetValue = 400f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "shimmer"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF07172C))
            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    listOf(Color.Transparent, Color(0x3338BDF8), Color.Transparent),
                    start = Offset(shimmerX, 0f),
                    end = Offset(shimmerX + 80f, size.height)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💎", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "منشور الماس کریستال (Diamond Prism)",
                        fontSize = 9.sp,
                        color = Color(0xFFBAE6FD)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x66082F49),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7DD3FC),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 15. Executive Leather (PRO) **/
@Composable
fun ExecutiveLeatherLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1917))
            .border(1.dp, Color(0xFFD97706), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💼", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFF5F5F4)
                    )
                    Text(
                        text = "چرم ناپا و برنز دست‌ساز",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF292524),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.5f))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFBBF24),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/** 16. Analog Celestial (PRO) **/
@Composable
fun AnalogCelestialLivePreview(jalaliDate: JalaliDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0B1120))
            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👑", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "داشبورد سلطنتی زمان و تقویم",
                        fontSize = 9.sp,
                        color = Color(0xFFFDE68A)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x660284C7),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7DD3FC),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 17. Hologram Grid (PRO - Radar Scanner) **/
@Composable
fun HologramGridLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "radar")
    val scanOffset by infTransition.animateFloat(
        initialValue = -50f,
        targetValue = 350f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing)),
        label = "scan"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF04121F))
            .border(1.dp, Color(0xFF00F0FF), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawLine(
                color = Color(0xFF00F0FF).copy(alpha = 0.5f),
                start = Offset(scanOffset, 0f),
                end = Offset(scanOffset, size.height),
                strokeWidth = 2f
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌐", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "رادار سایبرپانک • " + jalaliDate.dayOfWeekSimple,
                        fontSize = 9.sp,
                        color = Color(0xFF00F0FF)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0x66000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.5f))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F0FF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/** 18. Royal Emerald (PRO) **/
@Composable
fun RoyalEmeraldLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "emerald")
    val jewelAlpha by infTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "jewel"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF064E3B))
            .border(1.dp, Color(0xFFFBBF24).copy(alpha = jewelAlpha), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⚜️", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "زمرد سلطنتی VIP",
                        fontSize = 9.sp,
                        color = Color(0xFF6EE7B7)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x66022C22),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBBF24))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFDE68A),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 19. Frosted Liquid (PRO - Floating Bubbles) **/
@Composable
fun FrostedLiquidLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "bubbles")
    val b1Offset by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val b2Offset by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(3600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x33FFFFFF))
            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.25f),
                radius = 18f,
                center = Offset(size.width * 0.2f, size.height * 0.4f + b1Offset)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.18f),
                radius = 28f,
                center = Offset(size.width * 0.75f, size.height * 0.6f + b2Offset)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = 12f,
                center = Offset(size.width * 0.85f, size.height * 0.25f + b1Offset)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🫧", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "حباب‌های مایع ژلاتینی • " + jalaliDate.dayOfWeekSimple,
                        fontSize = 9.sp,
                        color = Color(0xFFBAE6FD)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x44000000)
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/** 20. Titanium Chronos (PRO - Rotating Dial) **/
@Composable
fun TitaniumChronosLivePreview(jalaliDate: JalaliDateInfo) {
    val infTransition = rememberInfiniteTransition(label = "dial")
    val dialAngle by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "dialRot"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0xFF94A3B8), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⏱️",
                    fontSize = 18.sp,
                    modifier = Modifier.rotate(dialAngle)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "کرونومتر تیتانیوم فضایی",
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0x55000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF94A3B8).copy(alpha = 0.5f))
            ) {
                Text(
                    text = jalaliDate.timePersian,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2E8F0),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
