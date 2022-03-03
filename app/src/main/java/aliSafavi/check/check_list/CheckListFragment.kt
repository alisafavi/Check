package aliSafavi.check.check_list

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentCheckListBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.xdev.arch.persiancalendar.datepicker.calendar.PersianCalendar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListFragment : Fragment() {

    private val viewModel : CheckListViewModel by viewModels()
    private lateinit var binding : FragmentCheckListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCheckListBinding.inflate(inflater, container, false)

        binding.btnNewCheck.setOnClickListener {
            it.findNavController().navigate(R.id.action_checkListFragment_to_checkFragment)
        }

        getDate()

        return binding.root
    }

    private fun getDate() {
        val p = PersianCalendar()
        p.timeInMillis=System.currentTimeMillis()

        Log.i("TAGG",p.toString())


    }

}