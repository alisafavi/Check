package aliSafavi.check.person

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentPersonListBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class PersonListFragment : Fragment() {

    private lateinit var binding : FragmentPersonListBinding
    private val viewModel : PersonListViewModle by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonListBinding.inflate(inflater, container, false)


        binding.btnNewPerson.setOnClickListener {
            findNavController().navigate(R.id.action_personListFragment_to_personFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupList()
    }

    private fun setupList() {
        val adapter=PersonListAdapter(PersonClickListener {
            val bundel = bundleOf("personId" to it)
            findNavController().navigate(R.id.action_personListFragment_to_personFragment,bundel)
        })
        binding.personList.adapter=adapter

        viewModel.persons.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}