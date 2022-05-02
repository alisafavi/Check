package aliSafavi.check.bank

import aliSafavi.check.R
import aliSafavi.check.bank.BankFragment.Companion.BankLogoDir
import aliSafavi.check.utils.bankCode
import android.app.Person
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.xdev.arch.persiancalendar.datepicker.calendar.PersianCalendar
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@BindingAdapter("setFromAssets")
fun setFromAssets(view: ShapeableImageView, accountNo: Long?) {
    accountNo?.let {
        try {
            var num:Long =it
            while (num>1000000)
                num /= 10
            val bankName = bankCode[num.toInt()]
            view.context.assets.open("$BankLogoDir/$bankName.png").use {input->
                Drawable.createFromStream(input, null)?.let {draw->
                    view.setImageDrawable(draw)
                }
            }
        } catch (e: FileNotFoundException) {
            view.setImageResource(R.drawable.ic_image)
            Log.e("input stream", e.toString())
        }
    }
}

@BindingAdapter("convertDate")
fun convertDate(view: TextView, date: Long) {
    if (date != 0L) {
        val date = PersianCalendar(date)
        date.month++
        view.setText(date.toString())
    }
}

@BindingAdapter("numberSeparator")
fun numberSeparator(view : TextView,number : Long){
    val decimalFormat = DecimalFormat("###,###,###,###,###,###", DecimalFormatSymbols(Locale.US))
    view.text=decimalFormat.format(number)
}