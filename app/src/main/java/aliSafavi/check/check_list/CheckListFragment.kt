package aliSafavi.check.check_list

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentCheckListBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListFragment : Fragment() {

    private val viewModel : CheckListViewModel by viewModels()
    private lateinit var binding : FragmentCheckListBinding
    private lateinit var checkList : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCheckListBinding.inflate(inflater, container, false)

        checkList = binding.checkList.apply {
            layoutManager=GridLayoutManager(requireContext(),1)
        }
        binding.btnNewCheck.setOnClickListener {
            it.findNavController().navigate(R.id.action_checkListFragment_to_checkFragment)
        }

        setupList()

        return binding.root
    }

    private fun setupList() {
        val adapter = CheckListAdapter(OnCheckItemClickListener {
            val bundel = bundleOf("checkId" to it)
            findNavController().navigate(R.id.action_checkListFragment_to_checkFragment,bundel)
        })

        checkList.adapter=adapter
        viewModel.getChecks()
        viewModel.checks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

    }

}
