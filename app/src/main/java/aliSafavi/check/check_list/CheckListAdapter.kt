package aliSafavi.check.check_list

import aliSafavi.check.databinding.CheckItemBinding
import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CheckListAdapter(private val itemClickListener: OnCheckItemClickListener) :
    ListAdapter<FullCheck, CheckListAdapter.ViewHolder>(CheckDiffCallback()) {
    class ViewHolder constructor(val binding: CheckItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FullCheck, clickListener: OnCheckItemClickListener) {
            binding.check = item
            binding.itemCardView.setOnClickListener {
                clickListener.onClick(item.check.cId)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CheckItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }

    fun getCheckId(position :Int) =getItem(position).check.cId
}


class OnCheckItemClickListener(val clickListener: (checkId: Long) -> Unit) {
    fun onClick(checkId: Long) = clickListener(checkId)
}

private class CheckDiffCallback : DiffUtil.ItemCallback<FullCheck>() {
    override fun areItemsTheSame(oldItem: FullCheck, newItem: FullCheck): Boolean {
        return oldItem.check.cId == newItem.check.cId
    }

    override fun areContentsTheSame(oldItem: FullCheck, newItem: FullCheck): Boolean {
        return oldItem == newItem
    }

}