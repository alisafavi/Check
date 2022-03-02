package aliSafavi.check.check_list

import aliSafavi.check.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListFragment : Fragment() {

    private val viewModel : CheckListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_check_list, container, false)

        view.findViewById<Button>(R.id.btn).setOnClickListener {
            it.findNavController().navigate(R.id.action_checkListFragment_to_bankFragment)
        }

        return view
    }

}