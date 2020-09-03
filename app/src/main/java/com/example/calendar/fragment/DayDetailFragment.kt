package com.example.calendar.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.calendar.R
import com.example.calendar.instance.CaculateDate
import kotlinx.android.synthetic.main.fragment_day_detail.*
import java.text.FieldPosition

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_POS = "pos"

/**
 * A simple [Fragment] subclass.
 * Use the [DayDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val TAG = "TAG Day detail fragment"
    private var pos: Int? = null
//    private var dd: Int? = null
//    private var mm: Int? = null
//    private var yyyy: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pos = it.getInt(ARG_POS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {

            tvNgayDuong.text = (pos?.minus(Int.MAX_VALUE)).toString()
//            tvThu.text = CaculateDate.getThu(dd!!, mm!!, yyyy!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(pos: Int) =
            DayDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POS, pos)
                }
            }
    }
}
