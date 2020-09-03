package com.example.calendar.instance

import android.util.Log

class CaculateDate {
    companion object {
        val TAG = "TAG Caculate Date"
        val Thu = arrayOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")
        val Chi =
            arrayOf(
                "Tý",
                "Sửu",
                "Dần",
                "Mẹo",
                "Thìn",
                "Tỵ",
                "Ngọ",
                "Mùi",
                "Thân",
                "Dậu",
                "Tuất",
                "Hợi"
            )
        val Can = arrayOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")

        /// Tìm tên gọi Thứ của ngày
        fun getThu(dd: Int, mm: Int, yy: Int): String {
            val jd = jdFromDate(dd, mm, yy)
            var index = (jd % 7)
            return Thu.get(index)
        }


        /// Tìm tên gọi Chi của năm (12 chi)
        fun getChiNam(yy: Int): String {
            return Chi.get((yy + 8) % 12)
        }


        /// Tìm tên gọi Can của năm (10 can)
        fun getCanNam(yy: Int): String? {
            return Can.get((yy + 6) % 10)
        }


        /// Tìm tên gọi Can của ngày
        fun getCanNgay(dd: Int, mm: Int, yy: Int): String? {
            val jd = jdFromDate(dd, mm, yy)
            return Can.get((jd + 9) % 10)
        }


        /// Tìm tên gọi Chi của ngày
        fun getChiNgay(dd: Int, mm: Int, yy: Int): String {
            val jd = jdFromDate(dd, mm, yy)
            return Chi.get((jd + 1) % 12)
        }


        /// Tìm tên gọi Chi của tháng (!tháng Âm Lịch)
        // mm la thang Am lich duoc tinh truoc do
        fun getChiThang(mm: Int): String? {
            val tam = (mm) % 12 // Thang 11 la thang Ty, thang 12 la thang Suu
            return Chi.get(tam)
        }

        // mm la thang am lich, yy nam am lich
        fun getCanThang(mm: Int, yy: Int): String? {
            val tam = (yy * 12 + mm + 2) % 10
            return Can.get(tam)
        }

        fun jdToDate(jd: Long): ArrayList<Int> {
            val a: Long
            val b: Long
            val c: Long
            val d: Long
            val e: Long
            val m: Long
            val day: Int
            val month: Int
            val year: Int

            // After 5/10/1582, Gregorian calendar
            if (jd > 2299160) {
                a = jd + 32044
                b = (((4 * a + 3) / 146097.0f).toInt()).toLong()
                c = a - (b * 146097 / 4.0f).toInt()
            } else {
                b = 0
                c = jd + 32082
            }
            d = (((4 * c + 3) / 1461.0f).toInt()).toLong()
            e = c - (1461 * d / 4.0f).toInt()
            m = (((5 * e + 2) / 153.0f).toInt()).toLong()
            day = (e - ((153 * m + 2) / 5.0f).toInt() + 1).toInt()
            month = (m + 3 - 12 * (m / 10.0f).toInt()).toInt()
            year = (b * 100 + d - 4800 + (m / 10.0f).toInt()).toInt()
            var listDMY = arrayListOf<Int>(day, month, year)
            Log.d(TAG, "JD to date :  ${listDMY.get(0)}/${listDMY.get(1)}/${listDMY.get(2)}")
            return listDMY
        }

        fun jdFromDate(dd: Int, mm: Int, yy: Int): Int {
            val a = (14 - mm) / 12
            val y = yy + 4800 - a
            val m = mm + 12 * a - 3
            var jd = (dd
                    + ((153 * m + 2) / 5)
                    + (365 * y)
                    + (y / 4)) - (y / 100) + (y / 400) - 32045
            if (jd < 2299161) {
                jd = dd + ((153 * m + 2) / 5) + 365 * y + (y / 4) - 32083
            }

            Log.d(TAG, "JD: $jd")
            return jd
        }

        // Tinh ngay Soc
        fun getNewMoonDay(k: Int): Int {
            var timeZone = 7
            // T, T2, T3, dr, Jd1, M, Mpr, F, C1, deltat, JdNew;
            val T = k / 1236.85f // Time in Julian centuries from 1900 January 0.5
            val T2 = T * T
            val T3 = T2 * T
            val dr = Math.PI / 180.0f
            var Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3
            Jd1 =
                Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr) // Mean new moon
            val M =
                359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3 // Sun's mean anomaly
            val Mpr =
                306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3 // Moon's mean anomaly
            val F =
                21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3 // Moon's argument of latitude
            var C1 =
                (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M)
            C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr)
            C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr)
            C1 =
                C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr))
            C1 =
                C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M))
            C1 =
                C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr))
            C1 =
                C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M))
            val deltat: Double
            deltat = if (T < -11) {
                0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3
            } else {
                -0.000278 + 0.000265 * T + 0.000262 * T2
            }
            val JdNew = Jd1 + C1 - deltat
            return (JdNew + 0.5 + timeZone / 24.0f).toInt()
        }


        // Trung khi
        fun getSunLongitude(jdn: Double): Int {
            var timeZone = 7
            //double T, T2, dr, M, L0, DL, L;
            val T =
                (jdn - 2451545.5 - timeZone / 24) / 36525 // Time in Julian centuries from 2000-01-01 12:00:00 GMT
            val T2 = T * T
            val dr = Math.PI / 180 // degree to radian
            val M =
                357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2 // mean anomaly, degree
            val L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2 // mean longitude, degree
            var DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M)
            DL =
                DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(
                    dr * 3 * M
                )
            var L = L0 + DL // true longitude, degree
            L = L * dr
            L =
                L - Math.PI * 2 * (L / (Math.PI * 2)).toInt() // Normalize to (0, 2*PI)
            return (L / Math.PI * 6).toInt()
        }

        //ERROR
        // Tim ngay bat dau thang 11 am lich
        fun getLunarMonth11(yy: Int): Int {
            val off = jdFromDate(31, 12, yy) - 2415021 // truoc 31/12/yy
            val k = (off / 29.530588853).toInt()
            var nm = getNewMoonDay(k).toDouble()// tim ngay soc truoc 31/12/yy
            val sunLong = getSunLongitude(nm) // sun longitude at local midnight
            if (sunLong >= 9) // Neu thang bat dau vau ngay soc do khong co dong chi,
            {
                nm =
                    getNewMoonDay(k - 1).toDouble()// thi lui 1 thang va tinh lai ngay soc
            }
            return nm.toInt()
        }

        // Xac dinh thang nhuan
        fun getLeapMonthOffset(a11: Long): Int {
            val k: Int
            var last: Int
            var arc: Int
            var i: Int
            k = ((a11 - 2415021.076998695) / 29.530588853 + 0.5).toInt()
            last = 0
            i = 1 // We start with the month following lunar month 11
            arc = getSunLongitude(getNewMoonDay(k + i).toDouble())
            do {
                last = arc
                i++
                arc = getSunLongitude(getNewMoonDay(k + i).toDouble())
            } while (arc != last && i < 14)
            return i - 1
        }


        // Duong lich sang Am lich
        fun convertSolar2Lunar(dd: Int, mm: Int, yy: Int): ArrayList<Int>? {
            var timeZone = 7
            val dayNumber = jdFromDate(dd, mm, yy)
            val k = ((dayNumber - 2415021.076998695) / 29.530588853).toInt()
            var monthStart = getNewMoonDay(k + 1)
            if (monthStart > dayNumber) {
                monthStart = getNewMoonDay(k)
            }
            var a11 = getLunarMonth11(yy)
            var b11 = a11
            var lunarYear: Int
            if (a11 >= monthStart) {
                lunarYear = yy
                a11 = getLunarMonth11(yy - 1)
            } else {
                lunarYear = yy + 1
                b11 = getLunarMonth11(yy + 1)
            }
            val diff = ((monthStart - a11) / 29)
            var lunarLeap = 0
            var lunarMonth = diff + 11
            if (b11 - a11 > 365) {
                val leapMonthDiff = getLeapMonthOffset(a11.toLong())
                if (diff >= leapMonthDiff) {
                    lunarMonth = diff + 10
                    if (diff === leapMonthDiff) {
                        lunarLeap = 1
                    }
                }
            }
            if (lunarMonth > 12) {
                lunarMonth = lunarMonth - 12
            }
            if (lunarMonth >= 11 && diff < 4) {
                lunarYear -= 1
            }
            var list = arrayListOf<Int>((dayNumber - monthStart + 1), lunarMonth, lunarYear)
            Log.d(TAG, "Duong sang am : $list")
            return list
        }

        // Am lich sang Duong lich
        fun convertLunar2Solar(
            lunarDay: Int,
            lunarMonth: Int,
            lunarYear: Int,
            lunarLeap: Int
        ): ArrayList<Int> {
            val k: Int
            var off: Int
            val leapOff: Int
            val a11: Long
            val b11: Long
            var leapMonth: Long
            val monthStart: Long
            if (lunarMonth < 11) {
                a11 = getLunarMonth11(lunarYear - 1).toLong()
                b11 = getLunarMonth11(lunarYear).toLong()
            } else {
                a11 = getLunarMonth11(lunarYear).toLong()
                b11 = getLunarMonth11(lunarYear + 1).toLong()
            }
            off = lunarMonth - 11
            if (off < 0) {
                off += 12
            }
            if (b11 - a11 > 365) {
                leapOff = getLeapMonthOffset(a11)
                leapMonth = leapOff - 2.toLong()
                if (leapMonth < 0) {
                    leapMonth += 12
                }
                if (lunarLeap != 0 && lunarMonth.toLong() != leapMonth) {
                    return arrayListOf<Int>(0, 0, 0)
                } else if (lunarLeap != 0 || off >= leapOff) {
                    off += 1
                }
            }
            k = (0.5 + (a11 - 2415021.076998695) / 29.530588853).toInt()
            monthStart = getNewMoonDay(k + off).toLong()
            return jdToDate(monthStart + lunarDay - 1)
        }

        fun isLeapYear(yy: Int): Boolean {
            if ((yy % 4 == 0 && yy % 100 != 0) || yy % 400 == 0) {
                return true
            }
            return false
        }

        fun isRealDate(dd: Int, mm: Int, yy: Int): Boolean {
            if (!(yy > 0 && mm > 0)) {
                return false; // Ngày không còn hợp lệ nữa!
            }
            // Kiểm tra tháng
            if (!(mm >= 1 && mm <= 12)) {
                return false; // Ngày không còn hợp lệ nữa!
            }
            // Kiểm tra ngày
            if (!(dd >= 1 && dd <= getDayOfMonth(mm, yy))) {
                return false; // Ngày không còn hợp lệ nữa!
            }
            return true
        }

        fun getDayOfMonth(mm: Int, yy: Int): Int {
            when (mm) {
                1, 3, 5, 7, 8, 10, 12 -> return 31
                4, 6, 9, 11 -> 30
                2 -> {
                    if (isLeapYear(yy)) {
                        return 29
                    }
                    return 28
                }
            }
            return -1
        }

        fun getNextDay(dd: Int, mm: Int, yy: Int): ArrayList<Int> {
            var dayOfMonth = getDayOfMonth(mm, yy)
            if (dayOfMonth == -1 || dd < 1 || dd > dayOfMonth) {
                return arrayListOf(-1, -1, -1)
            } else {
                if (dd < dayOfMonth) return arrayListOf(dd + 1, mm, yy)
                else if (mm == 12) {
                    return arrayListOf(1, 1, yy + 1)
                } else {
                    return arrayListOf(1, mm + 1, yy)
                }
            }

        }

        fun getDayBefore(dd: Int, mm: Int, yy: Int): ArrayList<Int> {
            if (dd - 1 == 0) {
                if (mm - 1 == 0) {
                    return arrayListOf(31, 12, yy - 1)
                } else {
                    return arrayListOf(getDayOfMonth(mm - 1, yy), mm - 1, yy)
                }
            } else {
                return arrayListOf(dd - 1, mm, yy)
            }

        }

        fun getChiGio(hh:Int){

        }


    }


}