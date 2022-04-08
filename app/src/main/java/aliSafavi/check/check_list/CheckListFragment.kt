package aliSafavi.check.check_list

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentCheckListBinding
import aliSafavi.check.utils.setupSnackbar
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
    private var lastCheck =0L


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
            val bundle = bundleOf("lastCheck" to lastCheck)
            it.findNavController().navigate(R.id.action_checkListFragment_to_checkFragment,bundle)
        }

        setupList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSnakbar()
    }

    private fun setupList() {
        val adapter = CheckListAdapter(OnCheckItemClickListener {
            val bundel = bundleOf("checkId" to it)
            findNavController().navigate(R.id.action_checkListFragment_to_checkFragment,bundel)
        })

        checkList.adapter=adapter
        viewModel.checks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (!it.isEmpty()){
                lastCheck = it[it.size-1].check.cId
            }
        })
    }

    private fun setupSnakbar() {
        viewModel.checkUpdatedEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                view?.setupSnackbar(it)
            }
        )
    }

}
