package aliSafavi.check.bank

import aliSafavi.check.model.Bank
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BankListAdapter (private val clickListener : onBankClickListener) :
    ListAdapter<Bank,RecyclerView.ViewHolder>(BankDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class onBankClickListener(val clickListener : (bankId : Int) -> Unit) {
    fun onClick(bank : Bank) = clickListener(bank.bId)
}

private class BankDiffCallback : DiffUtil.ItemCallback<Bank>() {
    override fun areItemsTheSame(oldItem: Bank, newItem: Bank): Boolean {
        return oldItem.bId == newItem.bId
    }

    override fun areContentsTheSame(oldItem: Bank, newItem: Bank): Boolean {
        return oldItem == newItem
    }
}
