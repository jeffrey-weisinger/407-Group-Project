package com.cs407.groupproject407

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import org.json.JSONArray
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)


    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, AddActivity::class.java)
            intent.putExtra("year", day.date.year)
            intent.putExtra("month", day.date.month.value)
            intent.putExtra("day", day.date.dayOfMonth)
            view.context.startActivity(intent)

        }
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {

    val titlesContainer = view as ViewGroup
}


class Calendar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        // Setup the day binder for the CalendarView
        val calendarView = view?.findViewById<CalendarView>(R.id.calendarView)
        calendarView?.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()

                val socialMarker = container.view.findViewById<View>(R.id.social_marker)
                val meetMarker = container.view.findViewById<View>(R.id.meeting_marker)
                val recMarker = container.view.findViewById<View>(R.id.recreation_marker)
                val schoolMarker = container.view.findViewById<View>(R.id.school_marker)
                val workMarker = container.view.findViewById<View>(R.id.work_marker)

                val sharedPref = requireContext().getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
                val jsonArr = JSONArray(sharedPref.getString("userData", ""))

                val formattedDate = data.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                for (i in 0 until jsonArr.length()) {
                    val jsonObject = jsonArr.getJSONObject(i)
                    if (jsonObject.getString("date") == formattedDate) {
                        val type = jsonObject.getString("activityType")
                        when (type) {
                            "Work" -> workMarker.visibility = View.VISIBLE
                            "School" -> schoolMarker.visibility = View.VISIBLE
                            "Recreation" -> recMarker.visibility = View.VISIBLE
                            "Meeting" -> meetMarker.visibility = View.VISIBLE
                            "Social" -> socialMarker.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth
        val endMonth = currentMonth.plusMonths(12) // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        calendarView?.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView?.scrollToMonth(currentMonth)

        val daysOfWeek = daysOfWeek()
        calendarView?.setup(startMonth, endMonth, daysOfWeek.first())

        val titlesContainer = view?.findViewById<ViewGroup>(R.id.titlesContainer1)
        titlesContainer?.children
            ?.map { it.findViewById<TextView>(R.id.calendarDayText) }
            ?.forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        //month header stuff
        calendarView?.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.yearMonth

                    val monthNames = listOf(
                        "January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"
                    )

                    val monthIndex = data.yearMonth.monthValue - 1
                    val monthName = monthNames[monthIndex]

                    container.titlesContainer.children.map { it.findViewById<TextView>(R.id.calendarDayText) }
                        .forEachIndexed { index, textView ->
                            if (index == 0) {
                                textView.text = monthName
                            } else {
                                textView.text = ""
                            }
                        }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Calendar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
