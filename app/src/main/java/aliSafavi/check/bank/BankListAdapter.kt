package aliSafavi.check.bank

import aliSafavi.check.databinding.BankItemBinding
import aliSafavi.check.model.Bank
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BankListAdapter (private val clickListener : BankClickListener) :
    ListAdapter<Bank,RecyclerView.ViewHolder>(BankDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BankListViewHolder(
            BankItemBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BankListViewHolder).bind(getItem(position),clickListener)
    }

    class BankListViewHolder(private val binding : BankItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(bank: Bank, clickListener: BankClickListener){
            binding.bank=bank
            binding.bankItem.setOnClickListener {
                clickListener.onClick(bank)
            }
        }
    }
}

class BankClickListener(val clickListener : (bankId : Int) -> Unit) {
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
