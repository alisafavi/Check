package aliSafavi.check.person

import aliSafavi.check.databinding.PersonItemBinding
import aliSafavi.check.model.Person
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PersonListAdapter (private val clickListener : PersonClickListener) :
    ListAdapter<Person,RecyclerView.ViewHolder>(PersonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonListViewHolder(
            PersonItemBinding.inflate(inflater)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonListViewHolder).bind(getItem(position),clickListener)
    }

    class PersonListViewHolder(private val binding : PersonItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(person: Person, clickListener: PersonClickListener){
            binding.person=person
            binding.personItem.setOnClickListener {
                clickListener.onClick(person)
            }
        }
    }
}

class PersonClickListener(val clickListener : (personId : Int) -> Unit) {
    fun onClick(person : Person) = clickListener(person.pId)
}

private class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.pId == newItem.pId
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}
