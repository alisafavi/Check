package aliSafavi.check.bank

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentBankListBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BankListFragment : Fragment() {

    private lateinit var binding: FragmentBankListBinding
    private val viewModel: BankListViewModel by viewModels()

    lateinit var btnNewBank: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bank_list, container, false)

        btnNewBank = binding.btNewBank.also {
            it.setOnClickListener {
                findNavController().navigate(R.id.action_bankListFragment_to_bankFragment)
            }
        }


        setupListView()
        return binding.root
    }

    private fun setupListView() {
        val adapter = BankListAdapter(BankClickListener {
            val bundle = bundleOf("bankId" to it)
            findNavController().navigate(R.id.action_bankListFragment_to_bankFragment, bundle)
        })
        binding.bankList.layoutManager = GridLayoutManager(requireContext(),1)
        binding.bankList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: BankListAdapter) {
        viewModel.banks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}