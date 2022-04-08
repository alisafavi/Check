package aliSafavi.check.check

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentCheckBinding
import aliSafavi.check.reciver.AlarmReciver
import aliSafavi.check.utils.NumberToText
import aliSafavi.check.utils.setupDatePicker
import aliSafavi.check.utils.setupSnackbar
import aliSafavi.check.utils.setupTimePicker
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.xdev.arch.persiancalendar.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.xdev.arch.persiancalendar.datepicker.calendar.PersianCalendar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CheckFragment : Fragment() {

    private val viewModel: CheckViewModel by viewModels()
    private val args: CheckFragmentArgs by navArgs()

    private lateinit var binding: FragmentCheckBinding

    private lateinit var etCheckNumber: TextInputEditText
    private lateinit var etCheckAmount: TextInputEditText
    private lateinit var etnCheckDate: TextInputEditText
    private lateinit var etCheckReciver: MaterialAutoCompleteTextView
    private lateinit var etCheckAccount: MaterialAutoCompleteTextView

    private var remider = Calendar.getInstance()
    private var onceRemind = true

    private var date = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
        }

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()

        viewModel.start(args.checkId)
        setupNavigation()
        setupSnakbar()
        setupReminder()
    }

    private fun setupReminder() {
        binding.btnReminder.run {
            setOnCheckedChangeListener { buttonView, isChecked ->
                when (isChecked) {
                    false -> binding.reminderParent.visibility = View.GONE
                    true -> {
                        binding.reminderParent.visibility = View.VISIBLE
                        if (onceRemind) {
                            onceRemind = false
                            if (date == 0L)
                                date = viewModel.date
                            remider.timeInMillis = date
                            remider.add(Calendar.DAY_OF_MONTH, -1)
                            remider.set(Calendar.HOUR_OF_DAY, 21)
                            remider.set(Calendar.MINUTE, 0)
                        }
                        if (date != 0L)
                            PersianCalendar().run {
                                timeInMillis = remider.timeInMillis
                                binding.etDateRemind.setText("$year/${month + 1}/$day")
                                binding.etTimeRemind.setText(
                                    "${get(Calendar.HOUR_OF_DAY)}:${get(Calendar.MINUTE)}"
                                )
                            }
                    }
                }
            }
        }
        binding.etDateRemind.run {
            setOnClickListener {
                setupDatePicker().let {
                    it.show(childFragmentManager, "reminder")
                    it.addOnPositiveButtonClickListener(object :
                        MaterialPickerOnPositiveButtonClickListener<Long?> {
                        override fun onPositiveButtonClick(selection: Long?) {
                            Calendar.getInstance().run {
                                timeInMillis = selection!!
                                remider.set(
                                    get(Calendar.YEAR),
                                    get(Calendar.MONTH),
                                    get(Calendar.DAY_OF_MONTH)
                                )
                            }
                            PersianCalendar(selection!!).run {
                                month++
                                binding.etDateRemind.setText(toString())
                            }
                        }
                    })
                }
            }
        }
        binding.etTimeRemind.run {
            setOnClickListener {
                setupTimePicker().let { timePicker ->
                    timePicker.show(childFragmentManager, "time reminder")
                    timePicker.addOnPositiveButtonClickListener {
                        Calendar.getInstance().run {
                            remider.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                            remider.set(Calendar.MINUTE, timePicker.minute)
                        }
                        setText("${timePicker.hour}:${timePicker.minute}")
                    }
                }
            }
        }
    }

    private fun setAlarm() {
        view?.setupTimePicker()?.let { timePicker ->
            timePicker.show(childFragmentManager, "time picker Tag")
            timePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, timePicker.hour)
                    add(Calendar.SECOND, timePicker.minute)
                }
                val intent = Intent(requireActivity(), AlarmReciver::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("checkId", etCheckAmount.text.toString())
//                    putExtra("")
                }

                val requestCode = if (args.lastCheck != 0L) args.lastCheck else args.checkId
                val pendingIntent =
                    PendingIntent.getBroadcast(context, requestCode.toInt() + 1, intent, 0)

                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
                    )
                }
            }
        }
    }

    private fun initView() {
        etCheckNumber = binding.etCheckNumber
        etCheckAmount = binding.etCheckAmount.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val et_amount = binding.etCheckAmount
                    val input = et_amount.text.toString().replace(",", "")
                    if (!input.isEmpty()) {
                        et_amount.removeTextChangedListener(this)
                        val decimalFormat = DecimalFormat(",###")
                        val text = decimalFormat.format(input.toBigInteger())
                        et_amount.setText(text)
                        et_amount.setSelection(text.length)
                        et_amount.addTextChangedListener(this)

                        binding.etCheckAmountParent.helperText =
                            NumberToText(input.toString()).toString() + " تومان"
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isEmpty())
                        binding.etCheckAmountParent.helperText = null
                }

            })

        }
        etnCheckDate = binding.etCheckDate.apply {
            setOnClickListener { getDate() }
        }
        etCheckReciver = binding.etCheckReciver.apply {
            setAdapter(getPersonAdapter())
        }
        etCheckAccount = binding.etCheckAccount.apply {
            setAdapter(getBankAdapter())
        }
        binding.btnOk.apply {
            setOnClickListener { save() }
            if (args.checkId != 0L)
                text = "edit"
        }
        binding.btnCancel.setOnClickListener {
            exit()
        }
        binding.btnNewBank.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_bankFragment)
        }
        binding.btnNewReciver.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_personFragment)
        }
    }

    private fun getBankAdapter(): ArrayAdapter<String> {
        val data = ArrayList<String>()
        runBlocking {
            viewModel.getBanksName().forEach {
                data.add(it)
            }
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        return adapter
    }

    private fun getPersonAdapter(): ArrayAdapter<String> {
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
        view?.setupDatePicker()?.let {
            it.show(childFragmentManager, "Check Date Picker Tag")

            it.addOnPositiveButtonClickListener(object :
                MaterialPickerOnPositiveButtonClickListener<Long?> {
                override fun onPositiveButtonClick(selection: Long?) {
                    date = selection!!
                    val date = PersianCalendar(selection!!)
                    date.month++
                    etnCheckDate.setText(date.toString())
                }
            })
        }
    }

    private fun validateForm(): Boolean {
        var status = true
        if (etCheckNumber.text.toString().isEmpty()) {
            etCheckNumber.error = getString(R.string.empty_error)
            status = false
        } else if (!etCheckNumber.text.toString().isDigitsOnly()) {
            etCheckNumber.error = getString(R.string.invalid_value_error)
            status = false
        }
        if (etCheckAmount.text.toString().isEmpty()) {
            etCheckAmount.error = getString(R.string.empty_error)
            status = false
        } else if (!etCheckAmount.text.toString().replace(",", "").isDigitsOnly()) {
            etCheckAmount.error = getString(R.string.invalid_value_error)
            status = false
        }
        if (etnCheckDate.text.toString().isEmpty()) {
            etnCheckDate.error = getString(R.string.empty_error)
            status = false
        }
        return status
    }

    private fun save() {
        if (validateForm()) {
            try {
                if (date == 0L)
                    date = viewModel.date
                viewModel.save(
                    CheckPrewiew(
                        cId = args.checkId,
                        number = etCheckNumber.text.toString().trim().toLong(),
                        date = date,
                        amount = etCheckAmount.text.toString().replace(",", "").trim().toLong(),
                        personName = etCheckReciver.text.toString().trim(),
                        bankName = etCheckAccount.text.toString().trim()
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
        viewModel.navigateUp.observe(viewLifecycleOwner, Observer {
            if (it)
                exit()
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

    private fun exit() {
        findNavController().navigateUp()
    }
}