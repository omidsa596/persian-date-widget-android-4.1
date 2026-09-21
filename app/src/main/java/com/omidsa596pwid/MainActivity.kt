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

            // Realistic Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isPro) Color(0xFF0F1523) else Color(0xFF0E1726),
                        RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        if (isPro) Color(0x33F59E0B) else Color(0x22FFFFFF),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🕒  " + jalaliDate.timePersianSpaced,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPro) Color(0xFFFDE68A) else Color(0xEEFFFFFF)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = jalaliDate.dayOfWeekSimple + " " + jalaliDate.dayPersian + " " + jalaliDate.monthName + " " + jalaliDate.yearPersian,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = jalaliDate.gregorianDateString,
                        fontSize = 10.sp,
                        color = Color(0x99FFFFFF)
                    )
                }
            }

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
