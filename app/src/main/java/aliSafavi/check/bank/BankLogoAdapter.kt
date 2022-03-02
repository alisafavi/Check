package aliSafavi.check.bank

import aliSafavi.check.databinding.BankLogoItemBinding
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream

class BankLogoAdapter(context: Context, resource: Int, logos: ArrayList<BankLogo>) :
    ArrayAdapter<BankLogo>(context, resource, logos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater =  LayoutInflater.from(parent.context)
        val binding = BankLogoItemBinding.inflate(inflater)

        binding.bankLogo = getItem(position)

        return binding.root
    }
}

data class BankLogo(
    val bankName: String,
    val bankImgSrc: Drawable
)