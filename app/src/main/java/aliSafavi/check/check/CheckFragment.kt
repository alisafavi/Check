package aliSafavi.check.check

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentCheckBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.xdev.arch.persiancalendar.datepicker.MaterialDatePicker
import com.xdev.arch.persiancalendar.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.xdev.arch.persiancalendar.datepicker.calendar.PersianCalendar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class CheckFragment : Fragment() {

    private val viewModel: CheckViewModel by viewModels()
    private val args: CheckFragmentArgs by navArgs()

    private lateinit var binding: FragmentCheckBinding

    private lateinit var etCheckNumber: TextInputEditText
    private lateinit var etCheckAmount: TextInputEditText
    private lateinit var btnCheckDate: MaterialButton
    private lateinit var etCheckReciver: MaterialAutoCompleteTextView
    private lateinit var etCheckAccount: MaterialAutoCompleteTextView
    private lateinit var btnSave: Button

    private var date = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
        args?.let {

        }
        if (args.checkId != 0L)
            viewModel.initCheck(args.checkId)

        initView()


        return binding.root
    }

    private fun initView() {
        etCheckNumber = binding.etCheckNumber
        etCheckAmount = binding.etCheckAmount
        btnCheckDate = binding.btnCheckDate.apply {
            cornerRadius = 11
            setOnClickListener { getDate() }
        }
        etCheckReciver = binding.etCheckReciver.apply {
            setAdapter(reciverAdapter())
        }
        etCheckAccount = binding.etCheckAccount.apply {
            setAdapter(bankAdapter())
        }
        btnSave = binding.btnOk.apply {
            setOnClickListener { save() }
        }
        binding.btnNewBank.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_bankFragment)
        }
        binding.btnNewReciver.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_personFragment)
        }
    }

    private fun bankAdapter(): ArrayAdapter<String> {
        val data = ArrayList<String>()
        runBlocking {
            viewModel.getBankssName().forEach {
                data.add(it)
            }
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        return adapter
    }

    private fun reciverAdapter(): ArrayAdapter<String> {
        val data = ArrayList<String>()
        runBlocking {
            viewModel.getPersonsName().forEach {
                data.add(it)
            }
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        return adapter
    }

    fun getDate() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("تاریخ را انتخاب کنید.")
            .build()

        datePicker.show(childFragmentManager, "sss")
        datePicker.addOnPositiveButtonClickListener(object :
            MaterialPickerOnPositiveButtonClickListener<Long?> {
            override fun onPositiveButtonClick(selection: Long?) {
                date= selection!!
                val date = PersianCalendar(selection!!)
                date.month++
                btnCheckDate.setText(date.toString())
            }
        })
    }

    private fun save() {
        viewModel.save(
            CheckPrewiew(
                cId = args.checkId,
                number = etCheckNumber.text.toString().trim().toLong(),
                date = date,
                amount = etCheckAmount.text.toString().trim().toLong(),
                personName = etCheckReciver.text.toString().trim(),
                bankName = etCheckAccount.text.toString().trim()
            )
        )
    }
}