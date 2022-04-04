package aliSafavi.check.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.setupSnackbar(
    snackbarMessage: Int,
    timeLength: Int=Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, context.getString(snackbarMessage), timeLength).show()
}