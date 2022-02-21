package aliSafavi.check.person

import aliSafavi.check.model.Person
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PersonListAdapter (private val clickListener: onPersonClickListener) :
    ListAdapter<Person,RecyclerView.ViewHolder>(BankDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class onPersonClickListener(val clickListener : (personId : Int) -> Unit) {
    fun onClick(person: Person) = clickListener(person.pId)
}

private class BankDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.pId == newItem.pId
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}