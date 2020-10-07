package com.example.calendar.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendar.Interface.MonthCalendarInterface
import com.example.calendar.R
import com.example.calendar.adapter.CalendarAdapter
import com.example.calendar.instance.CaculateDate
import com.example.calendar.instance.DayMonthYearSelected.dayMonthYearSelected
import com.example.calendar.model.DayMonthYear
import com.example.calendar.model.DayMonthYearModel
import kotlinx.android.synthetic.main.fragment_month.*
import java.util.*

class MonthFragment : Fragment(), MonthCalendarInterface {
    val TAG = "TagMonthFragment"
    lateinit var calendar: Calendar
    lateinit var dmy: DayMonthYearModel
    var DAYS_COUNT = 35

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = Calendar.getInstance()
        dmy = DayMonthYearModel(
            this.calendar.get(Calendar.DAY_OF_MONTH),
            this.calendar.get(Calendar.MONTH) + 1,
            this.calendar.get(Calendar.YEAR)
        )
        return inflater.inflate(R.layout.fragment_month, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDateToView(dmy)
        setCalendarToView(calendar)
        openDatePickerDialog()
        choseToday()

    }

    fun choseToday() {
        btnToday.setOnClickListener {
            var cal = Calendar.getInstance()
            dmy = DayMonthYearModel(
                this.calendar.get(Calendar.DAY_OF_MONTH),
                this.calendar.get(Calendar.MONTH) + 1,
                this.calendar.get(Calendar.YEAR)
            )
            setCalendarToView(cal)
            setDateToView(dmy)
            dayMonthYearSelected = dmy
        }
    }

    fun openDatePickerDialog() {
        cardviewChoseDay.setOnClickListener {

            var cal = Calendar.getInstance()

            var datePickerDialog = DatePickerDialog(
                context!!,
                android.R.style.Theme_Material_Light_Dialog_MinWidth,
                { view, yyyy, mm, dd ->
                    dmy = DayMonthYearModel(dd, mm + 1, yyyy)
                    setDateToView(dmy)

                    var date = Date(yyyy - 1900, mm, dd)
                    cal.time = date
                    setCalendarToView(cal)
                    dayMonthYearSelected = dmy
                },
                dmy.yyyy,
                dmy.mm - 1,
                dmy.dd
            )
            datePickerDialog.show()

        }

    }

    /**    Hiển thị lịch tháng     */
    override fun setCalendarToView(cal: Calendar) {
        var cells = ArrayList<Date>()

        /* Ngày đầu tiên của tháng hiện tại*/
        var calendar = cal.clone() as Calendar

        /* Xắc định ô đầu tháng hiện tại*/
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        var monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2

        Log.d(
            TAG,
            "vi tri dau tien cua ngay dau thang $monthBeginningCell  calendar.get(Calendar.DAY_OF_WEEK) ${
                calendar.get(
                    Calendar.DAY_OF_WEEK
                )
            } "
        )
        if (monthBeginningCell == -1) {
            monthBeginningCell = 6
        }
        // Lấy ngày đầu tuần trong tuần chứa ngày đầu tháng
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        // Điền vào các ô
        while (cells.size < DAYS_COUNT) {
            cells.add(calendar.time)

            // day in question
            /*Thêm dòng cho những tháng đặc biệt*/
            if (cells.size == 35) {
                val lich = Calendar.getInstance()
                lich.time = cells.get(34)
                if (lich.get(Calendar.DATE) >= 28) {
                    DAYS_COUNT = 42
                } else {
                    DAYS_COUNT = 35
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)

        }
        // Update gridview
        gv_calendar?.adapter = CalendarAdapter(context, cells, this)
    }

    override fun setDateToView(dayMonthYear: DayMonthYearModel) {
        tv_day_of_week.text = "${CaculateDate.getThu(dayMonthYear)}"
        tv_solar_day.text = formatDateSolar(dayMonthYear)

        var dmyAm = CaculateDate.convertSolar2Lunar(dayMonthYear)
        tvNgayThangNamAm.text = formatDateSolar(dmyAm)
        tv_lunar_day.text = formatDateLunar(dmyAm)

        tvCanChiNgay.text =
            "Ngày ${CaculateDate.getCanNgay(dayMonthYear)} ${CaculateDate.getChiNgay(dayMonthYear)}"
        tvCanChiThang.text =
            " - Tháng ${CaculateDate.getCanThang(dmyAm)} ${CaculateDate.getChiThang(dmyAm)}"
        tvCanChiNam.text = " - Năm ${CaculateDate.getCanNam(dayMonthYear)} ${
            CaculateDate.getChiNam(
                dayMonthYear
            )
        }"

        if (CaculateDate.ngayHoangDao(
                DayMonthYear(
                    dayMonthYear.dd,
                    dayMonthYear.mm,
                    dayMonthYear.yyyy
                )
            ) == 1
        ) {
            tvNgayHoangDao.text = " *Ngày hoàng đạo*"
            tvNgayHoangDao.setTextColor(Color.RED)
            tvNgayHoangDao.visibility = View.VISIBLE
        } else if (CaculateDate.ngayHoangDao(
                DayMonthYear(
                    dayMonthYear.dd,
                    dayMonthYear.mm,
                    dayMonthYear.yyyy
                )
            ) == 0
        ) {
            tvNgayHoangDao.text = " *Ngày hắc đạo* "
            tvNgayHoangDao.setTextColor(Color.BLACK)
            tvNgayHoangDao.visibility = View.VISIBLE
        } else if (CaculateDate.ngayHoangDao(
                DayMonthYear(
                    dayMonthYear.dd,
                    dayMonthYear.mm,
                    dayMonthYear.yyyy
                )
            ) == -1
        ) {
            tvNgayHoangDao.visibility = View.INVISIBLE
        }

        var textGioHoangDao = StringBuilder()
        for (i in 0..CaculateDate.getGioHoangDaoChiGio(dayMonthYear)!!.size - 1) {
            textGioHoangDao.append(CaculateDate.getGioHoangDaoChiGio(dayMonthYear)!!.get(i))
            if (i == 2) {
                textGioHoangDao.append("\n")
            } else {
                if (i != 5) {
                    textGioHoangDao.append(" - ")
                }
            }

        }

        tvGioHoangDaoMonth.text = textGioHoangDao

    }


    fun formatDateSolar(dmy: DayMonthYearModel): String {
        var dateformat = ""
        if (dmy.mm < 10 && dmy.dd < 10) {
            dateformat = "0${dmy.dd} Tháng 0${dmy.mm}, ${dmy.yyyy}"
        } else if (dmy.mm < 10) {
            dateformat = "${dmy.dd} Tháng 0${dmy.mm}, ${dmy.yyyy}"
        } else if (dmy.dd < 10) {
            dateformat = "0${dmy.dd} Tháng ${dmy.mm}, ${dmy.yyyy}"
        } else {
            dateformat = "${dmy.dd} Tháng ${dmy.mm}, ${dmy.yyyy}"
        }
        return dateformat
    }

    fun formatDateLunar(dmy: DayMonthYearModel): String {
        var dateformat = ""
        if (dmy.mm < 10 && dmy.dd < 10) {
            dateformat = "0${dmy.dd} Tháng 0${dmy.mm} Âm lịch"
        } else if (dmy.mm < 10) {
            dateformat = "${dmy.dd} Tháng 0${dmy.mm} Âm lịch"
        } else if (dmy.dd < 10) {
            dateformat = "0${dmy.dd} Tháng ${dmy.mm} Âm lịch"
        } else {
            dateformat = "${dmy.dd} Tháng ${dmy.mm} Âm lịch"
        }
        return dateformat
    }


}
