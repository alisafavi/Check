package aliSafavi.check.utils

import aliSafavi.check.R
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.xdev.arch.persiancalendar.datepicker.MaterialDatePicker

fun View.setupSnackbar(
    snackbarMessage: Int,
    timeLength: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, context.getString(snackbarMessage), timeLength).show()
}

fun View.setupDatePicker(
    title: Int = R.string.select_date
) =
    MaterialDatePicker.Builder
        .datePicker()
        .setTitleText(context.getString(title))
        .build()

fun View.setupTimePicker(
    title: Int=R.string.select_time
) = MaterialTimePicker.Builder()
    .setTimeFormat(TimeFormat.CLOCK_24H)
    .setTitleText(context.getString(title))
    .build()