package aliSafavi.Check.check_list

import aliSafavi.Check.model.Check
import aliSafavi.Check.model.Person
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CheckListAdapter(private val clickListener: onCheckClickListener) :
    ListAdapter<Check,RecyclerView.ViewHolder>(CheckDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}


class onCheckClickListener(val clickListener: (checkId: Long) -> Unit){
    fun onClick(check : Check) = clickListener(check.cId)
}

private class CheckDiffCallback : DiffUtil.ItemCallback<Check>(){
    override fun areItemsTheSame(oldItem: Check, newItem: Check): Boolean {
        return oldItem.cId == newItem.cId
    }

    override fun areContentsTheSame(oldItem: Check, newItem: Check): Boolean {
        return oldItem == newItem
    }

}