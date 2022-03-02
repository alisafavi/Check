package aliSafavi.check.bank

import aliSafavi.check.bank.BankFragment.Companion.BankLogoDir
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

@BindingAdapter("setFromDrawble")
fun setFromDrawble(view: ShapeableImageView, drawable : Drawable){
    view.setImageDrawable(drawable)
}

@BindingAdapter("setFromAssets")
fun setFromAssets(view: ShapeableImageView,fName : String?){
    fName?.let {
        try {
            view.context.assets.open(BankLogoDir+"/"+fName).use {
                Drawable.createFromStream(it,null)?.let {
                    view.setImageDrawable(it)
                }
            }
        }catch (e: FileNotFoundException){
            Log.e("input stream",e.toString())
        }
    }
}

@BindingAdapter("withOutFiltering")
fun withOutFiltering(view: MaterialAutoCompleteTextView, text :String?){
    text?.let {
        view.setText(it.removeSuffix(".png"),false)
    }
}