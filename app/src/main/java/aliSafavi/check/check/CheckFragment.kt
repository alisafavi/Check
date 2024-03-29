package aliSafavi.check.check

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.bank.convertDate
import aliSafavi.check.databinding.FragmentCheckBinding
import aliSafavi.check.reciver.AlarmReciver
import aliSafavi.check.utils.NumberToText
import aliSafavi.check.utils.setupDatePicker
import aliSafavi.check.utils.setupSnackbar
import aliSafavi.check.utils.setupTimePicker
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.NOT_FOCUSABLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
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
import java.text.DecimalFormatSymbols
import java.util.*

enum class Mode {
    NEW, EDIT, VIEW
}

@AndroidEntryPoint
class CheckFragment : DialogFragment() {

    private val viewModel: CheckViewModel by viewModels()
    private val args: CheckFragmentArgs by navArgs()

    private lateinit var binding: FragmentCheckBinding

    private lateinit var etCheckNumber: TextInputEditText
    private lateinit var etCheckAmount: TextInputEditText
    private lateinit var etnCheckDate: TextInputEditText
    private lateinit var etCheckReciver: MaterialAutoCompleteTextView
    private lateinit var etCheckAccount: MaterialAutoCompleteTextView
    private lateinit var etTimeRemind: TextInputEditText
    private lateinit var etDateRemind: TextInputEditText

    private var remider = Calendar.getInstance().apply {
        timeInMillis = 0L
    }
    private var onceRemind = true
    private var date = 0L


    companion object {
        fun newInstance(bundle: Bundle): CheckFragment {
            val checkFragment = CheckFragment()
            checkFragment.arguments = bundle
            return checkFragment
        }
    }

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

        when (args.mode) {
            Mode.VIEW -> viewMode()
            Mode.EDIT -> editMode()
            else -> {}
        }

        viewModel.start(args.checkId)
        if (args.checkId == 0L)
            viewModel.startNum(args.checkNumber)
        setupNavigation()
        setupSnakbar()
        getDates()
        setupReminder()

    }

    private fun editMode() {
    }

    private fun viewMode() {
        binding.etCheckNumberParent.isEnabled = false
        binding.etCheckAmountParent.isEnabled = false
        binding.etCheckDateParent.isEnabled = false
        binding.etCheckReciverParent.isEnabled = false
        binding.etCheckAccountParent.isEnabled = false
        binding.etTimeRemindParent.isEnabled = false
        binding.etDateRemindParent.isEnabled = false
        binding.btnReminder.isEnabled = false
        binding.btnOk.run {
            isEnabled = false
            setOnClickListener(null)
            visibility = View.GONE
        }
        binding.btnCancel.run {
            isEnabled = false
            setOnClickListener(null)
            visibility = View.GONE
        }
        binding.btnNewReciver.visibility = View.GONE
        binding.btnNewBank.visibility = View.GONE
        binding.sveEditParent.visibility = View.GONE
    }

    override fun onResume() {
        viewModel.state?.let {
            etCheckNumber.setText(it["number"])
            etCheckAmount.setText(it["amount"])
            etnCheckDate.setText(it["date"])
            etCheckReciver.setText(it["reciver"])
            etCheckAccount.setText(it["account"])
            etDateRemind.setText(it["dateRemind"])
            etTimeRemind.setText(it["timeRemind"])
        }
        super.onResume()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.state?.let {
            etCheckNumber.setText(it["number"])
            etCheckAmount.setText(it["amount"])
            etnCheckDate.setText(it["date"])
            etCheckReciver.setText(it["reciver"])
            etCheckAccount.setText(it["account"])
            etDateRemind.setText(it["dateRemind"])
            etTimeRemind.setText(it["timeRemind"])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val number = etCheckNumber.text.toString()
        val amount = etCheckAmount.text.toString()
        val date = etnCheckDate.text.toString()
        val reciver = etCheckReciver.text.toString()
        val account = etCheckAccount.text.toString()
        val dateRemind = etDateRemind.text.toString()
        val timeRemind = etTimeRemind.text.toString()

        viewModel.state = mapOf(
            "number" to number, "amount" to amount,
            "date" to date, "reciver" to reciver, "account" to account,
            "dateRemind" to dateRemind, "timeRemind" to timeRemind
        )
    }

    private fun setupReminder() {
        etDateRemind.run {
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
                                etDateRemind.setText(toString())
                            }
                        }
                    })
                }
            }
        }
        etTimeRemind.run {
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

    private fun getDates() {
        viewModel.check.observe(viewLifecycleOwner, Observer {
            date = it.check.date
            remider.timeInMillis = it.check.reminderTime
            setupReminderCheckBox()
        })
        setupReminderCheckBox()
    }

    private fun setupReminderCheckBox() {
        binding.btnReminder.run {
            if (remider.timeInMillis != 0L) {
                isChecked = true
                binding.reminderParent.visibility = View.VISIBLE
                fillReminders()
            }
            setOnCheckedChangeListener { buttonView, isChecked ->
                when (isChecked) {
                    false -> {
                        remider.timeInMillis = 0L
                        binding.reminderParent.visibility = View.GONE
                    }
                    true -> {
                        binding.reminderParent.visibility = View.VISIBLE
                        fillReminders()
                    }
                }
            }
        }
    }

    private fun fillReminders() {
        if (onceRemind && date != 0L && remider.timeInMillis == 0L) {
            onceRemind = false
            remider.timeInMillis = date
            remider.add(Calendar.DAY_OF_MONTH, -1)
            remider.set(Calendar.HOUR_OF_DAY, 21)
            remider.set(Calendar.MINUTE, 0)
        }
        if (remider.timeInMillis != 0L)
            PersianCalendar().run {
                timeInMillis = remider.timeInMillis
                etDateRemind.setText("$year/${month + 1}/$day")
                etTimeRemind.setText(
                    "${get(Calendar.HOUR_OF_DAY)}:${get(Calendar.MINUTE)}"
                )
            }
    }

    private fun setAlarm(calendar: Calendar) {
        val intent = Intent(requireActivity(), AlarmReciver::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("checkId", args?.checkId)
            putExtra("name", etCheckReciver.text.toString())
            putExtra("date", calendar.timeInMillis)
            putExtra("amount", etCheckAmount.text.toString())
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

    private fun initView() {
        binding.reminderParent.visibility = View.GONE
        etDateRemind = binding.etDateRemind
        etTimeRemind = binding.etTimeRemind
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
                    val input = etCheckAmount.text.toString().replace(",", "")
                    if (!input.isEmpty()) {
                        etCheckAmount.removeTextChangedListener(this)
                        val decimalFormat = DecimalFormat(",###", DecimalFormatSymbols(Locale.US))
                        val text = decimalFormat.format(input.toBigInteger())
                        etCheckAmount.setText(text)
                        etCheckAmount.setSelection(text.length)
                        etCheckAmount.addTextChangedListener(this)

                        binding.etCheckAmountParent.helperText =
                            NumberToText(input).toString() + " تومان"
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
                    convertDate(etnCheckDate, selection!!)
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
                viewModel.save(getForms())
                if (binding.btnReminder.isChecked)
                    setAlarm(remider)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getForms(): CheckPrewiew = CheckPrewiew(
        cId = args.checkId,
        number = etCheckNumber.text.toString().trim()?.toLong(),
        date = date,
        amount = etCheckAmount.text.toString().replace(",", "").trim().toLong(),
        reminderTime = remider.timeInMillis,
        personName = etCheckReciver.text.toString().trim(),
        bankName = etCheckAccount.text.toString().trim()
    )

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