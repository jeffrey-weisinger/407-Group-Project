package com.cs407.groupproject407

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

    //add onlclick:
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            Log.i("asdf","asdf")

        }
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    // Alternatively, you can add an ID to the container layout and use findViewById()
    val titlesContainer = view as ViewGroup
}


class Calendar : Fragment() {
    // TODO: Rename and change types of parameters
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

        // Get reference to the CalendarView
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // Setup the day binder for the CalendarView
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            // Called only when a new container is needed
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            // Called every time we need to reuse a container
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                // Additional customization can go here
                // Example: Change background color for weekends, disable past dates, etc.
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth // currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(12) // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        val daysOfWeek = daysOfWeek()
        calendarView.setup(startMonth, endMonth, daysOfWeek.first())

        val titlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer1)
        titlesContainer.children
            .map { it.findViewById<TextView>(R.id.calendarDayText) }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }




        //month stuff
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            //given example code:
//            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
//                // Remember that the header is reused so this will be called for each month.
//                // However, the first day of the week will not change so no need to bind
//                // the same view every time it is reused.
//                if (container.titlesContainer.tag == null) {
//                    container.titlesContainer.tag = data.yearMonth
//                    container.titlesContainer.children.map { it.findViewById<TextView>(R.id.calendarDayText) }
//                        .forEachIndexed { index, textView ->
//
//                            val dayOfWeek = daysOfWeek[index]
//                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//
//
//                            textView.text = title
//                            // In the code above, we use the same `daysOfWeek` list
//                            // that was created when we set up the calendar.
//                            // However, we can also get the `daysOfWeek` list from the month data:
//                            // val daysOfWeek = data.weekDays.first().map { it.date.dayOfWeek }
//                            // Alternatively, you can get the value for this specific index:
//                            // val dayOfWeek = data.weekDays.first()[index].date.dayOfWeek
//                        }
//                }
//            }
            //test code
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Calendar.
         */
        // TODO: Rename and change types and number of parameters
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