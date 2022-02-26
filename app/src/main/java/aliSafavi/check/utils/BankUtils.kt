package aliSafavi.check.utils

import aliSafavi.check.R
import aliSafavi.check.model.Bank
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

//@BindingAdapter("bankImg")
//fun ShapeableImageView.setupImage(view: ShapeableImageView,bank : Bank){
//    bank.img?.let {
//        setBackgroundResource(R.drawable.spiderman_background)
//        setImageResource(R.mipmap.spiderman_foreground)
//    }
//}
//
//@BindingAdapter("bankNumber")
//fun TextInputEditText.setText(bank : Bank){
////    text = bank.accountNumber.toString()
////    setText(bank.accountNumber.toString())
//}

//@BindingAdapter("setText")
//fun TextView.setBankAccountNumber(bank: String){
////    text = bank.accountNumber.toString()
////    setText(bank.accountNumber.toString())
//    text = "saaaaaaa"
//}

@BindingAdapter("setText")
fun TextView.setText(bank : Bank){
//    bank?.let {
//        text = bank.accountNumber.toString()
//    }
}