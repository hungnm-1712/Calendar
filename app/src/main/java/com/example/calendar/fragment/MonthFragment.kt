package com.example.calendar.fragment

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.calendar.R
import com.example.calendar.instance.CaculateDate
import com.example.calendar.instance.DayMonthYear
import com.example.calendar.instance.Lunar
import com.example.calendar.model.DayMonthYearModel
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_month.*
import kotlinx.android.synthetic.main.fragment_month.tvCanChiNam
import kotlinx.android.synthetic.main.fragment_month.tvCanChiNgay
import kotlinx.android.synthetic.main.fragment_month.tvCanChiThang
import kotlinx.android.synthetic.main.fragment_month.tvThu
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DAY = "day"
private const val ARG_MONTH = "month"
private const val ARG_YEAR = "year"

/**
 * A simple [Fragment] subclass.
 * Use the [MonthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonthFragment : Fragment() {
    // TODO: Rename and change types of parameters


    val TAG = "TagMonthFragment"

    lateinit var calendar: Calendar
    lateinit var dmy: DayMonthYearModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        calendar = Calendar.getInstance()
        dmy = DayMonthYearModel(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )

        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setTextToLayout()
        calendarView.setOnDateChangeListener { view, year, month, dayofmonth ->
            dmy = DayMonthYearModel(dayofmonth, month + 1, year)
            setTextToLayout()
        }
    }

    fun setTextToLayout() {
        tvThu.text = "${CaculateDate.getThu(dmy)}"
        tvNgayThangNamDuong.text = formatDate(dmy)


        var dmyAm = CaculateDate.convertSolar2Lunar(dmy)
        tvNgayThangNamAm.text = formatDate(dmyAm)

        tvCanChiNgay.text =
            "Ngày ${CaculateDate.getCanNgay(dmy)} ${CaculateDate.getChiNgay(dmy)}"
        tvCanChiThang.text =
            " - Tháng ${CaculateDate.getCanThang(dmyAm)} ${CaculateDate.getChiThang(dmyAm)}"
        tvCanChiNam.text = " - Năm ${CaculateDate.getCanNam(dmy)} ${CaculateDate.getChiNam(dmy)}"

        if (CaculateDate.ngayHoangDao(DayMonthYear(dmy.dd, dmy.mm, dmy.yyyy)) == 1) {
            tvNgayHoangDao.text = " *Ngày hoàng đạo*"
            tvNgayHoangDao.setTextColor(Color.RED)
            tvNgayHoangDao.visibility = View.VISIBLE
        } else if (CaculateDate.ngayHoangDao(DayMonthYear(dmy.dd, dmy.mm, dmy.yyyy)) == 0) {
            tvNgayHoangDao.text = " *Ngày hắc đạo* "
            tvNgayHoangDao.setTextColor(Color.BLACK)
            tvNgayHoangDao.visibility = View.VISIBLE
        } else if (CaculateDate.ngayHoangDao(DayMonthYear(dmy.dd, dmy.mm, dmy.yyyy)) == -1) {
            tvNgayHoangDao.visibility = View.INVISIBLE
        }

        var textGioHoangDao = StringBuilder()
        for (i in 0..CaculateDate.getGioHoangDaoChiGio(dmy)!!.size - 1) {
            textGioHoangDao.append(CaculateDate.getGioHoangDaoChiGio(dmy)!!.get(i))
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

    fun formatDate(dmy: DayMonthYearModel): String {
        var dateformat = ""
        if (dmy.mm < 10 && dmy.dd < 10) {
            dateformat = "0${dmy.dd}/0${dmy.mm}/${dmy.yyyy}"
        } else if (dmy.mm < 10) {
            dateformat = "${dmy.dd}/0${dmy.mm}/${dmy.yyyy}"
        } else if (dmy.dd < 10) {
            dateformat = "0${dmy.dd}/${dmy.mm}/${dmy.yyyy}"
        } else {
            dateformat = "${dmy.dd}/${dmy.mm}/${dmy.yyyy}"
        }
        return dateformat
    }


}
