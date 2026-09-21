package com.omidsa596pwid

import java.util.Calendar

data class JalaliDateInfo(
    val year: Int,
    val month: Int,
    val day: Int,
    val monthName: String,
    val dayOfWeek: String,
    val dayOfWeekSimple: String,
    val yearPersian: String,
    val dayPersian: String,
    val timePersian: String,
    val timePersianSpaced: String,
    val gregorianDateString: String,
    val isDaytime: Boolean
)

object JalaliHelper {

    private val MONTH_NAMES = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    private val WEEKDAYS_STANDARD = arrayOf(
        "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه", "شنبه"
    )

    private val WEEKDAYS_SIMPLE = arrayOf(
        "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"
    )

    private val G_MONTHS = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    fun toPersianDigits(text: String): String {
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val sb = StringBuilder()
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(persianDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun getNow(): JalaliDateInfo {
        val cal = Calendar.getInstance()
        val gYear = cal.get(Calendar.YEAR)
        val gMonth = cal.get(Calendar.MONTH) + 1
        val gDay = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1

        val (jYear, jMonth, jDay) = gregorianToJalali(gYear, gMonth, gDay)

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val hourP = toPersianDigits(String.format("%02d", hour))
        val minP = toPersianDigits(String.format("%02d", minute))

        val isDaytime = hour in 6..18
        val gregorianMonthName = G_MONTHS[cal.get(Calendar.MONTH)]
        val gregorianDate = "$gDay $gregorianMonthName $gYear"

        return JalaliDateInfo(
            year = jYear,
            month = jMonth,
            day = jDay,
            monthName = MONTH_NAMES[jMonth - 1],
            dayOfWeek = WEEKDAYS_STANDARD[dayOfWeekIndex],
            dayOfWeekSimple = WEEKDAYS_SIMPLE[dayOfWeekIndex],
            yearPersian = toPersianDigits(jYear.toString()),
            dayPersian = toPersianDigits(jDay.toString()),
            timePersian = "$hourP:$minP",
            timePersianSpaced = "$hourP : $minP",
            gregorianDateString = gregorianDate,
            isDaytime = isDaytime
        )
    }

    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
        val gDaysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gy2 = gy - 1600
        var gm2 = gm - 1
        var gd2 = gd - 1

        var gDayNo = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400

        for (i in 0 until gm2) {
            gDayNo += gDaysInMonth[i + 1]
        }
        if (gm2 > 1 && ((gy2 % 4 == 0 && gy2 % 100 != 0) || (gy2 % 400 == 0))) {
            gDayNo++
        }
        gDayNo += gd2

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        val jm: Int
        val jd: Int
        if (jDayNo < 186) {
            jm = 1 + jDayNo / 31
            jd = 1 + (jDayNo % 31)
        } else {
            jm = 7 + (jDayNo - 186) / 30
            jd = 1 + ((jDayNo - 186) % 30)
        }

        return Triple(jy, jm, jd)
    }
}
