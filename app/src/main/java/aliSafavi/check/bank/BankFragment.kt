package aliSafavi.check.bank

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentBankBinding
import aliSafavi.check.model.Bank
import aliSafavi.check.utils.setupSnackbar
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class BankFragment : Fragment() {

    companion object {
        val BankLogoDir = "bank"
    }

    private lateinit var binding: FragmentBankBinding
    private val viewModel: BankViewModel by viewModels()
    private val args: BankFragmentArgs by navArgs()

    private lateinit var etBankName: AutoCompleteTextView
    private lateinit var etAccountName: TextInputEditText
    private lateinit var etBankNumber: TextInputEditText
    private lateinit var btnCancel: Button
    private lateinit var btnSaveEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBankBinding.inflate(inflater).also {
            it.viewModel = viewModel
        }

        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.start(args.bankId)

        initView()
        setupButtons()
        setupNavigation()
        setupSnakbar()

    }

    private fun setupButtons() {
        btnCancel.setOnClickListener {
            exit()
        }
        btnSaveEdit.apply {
            setOnClickListener { save() }
            if (args.bankId != 0)
                text = "edit"
        }
    }


    private fun initView() {
        etBankName = binding.etBankName.apply {
            val data = ArrayList<BankLogo>()
            runBlocking {
                requireContext().assets.list(BankLogoDir)?.map {
                    val inputStream = requireContext().assets.open(BankLogoDir + "/" + it)
                    val drawable = Drawable.createFromStream(inputStream, null)
                    data.add(
                        BankLogo(
                            it.removeSuffix(".png"),
                            drawable
                        )
                    )
                }
            }
            val adapter = BankLogoAdapter(requireContext(), R.layout.bank_logo_item, data)
            setAdapter(adapter)
            setOnItemClickListener { adapterView, view, position, l ->
                setText(data.get(position).bankName.removeSuffix(".png"), false)
                data.get(position).bankImgSrc?.let {
                    binding.bankImg.setImageDrawable(it)
                }
                error=null
            }
//            showDropDown()
        }

        etAccountName = binding.etAccountName
        etBankNumber = binding.etBankNumber
        btnCancel = binding.btnCancel
        btnSaveEdit = binding.btnSaveEdit
    }

    private fun validateForm(): Boolean {
        var status = true
        if (etBankName.text.toString().isEmpty()) {
            etBankName.error = getString(R.string.empty_error)
            status = false
        }
        if (etAccountName.text.toString().isEmpty()) {
            etAccountName.error = getString(R.string.empty_error)
            status = false
        }
        if(etBankNumber.text.toString().isEmpty()){
            etBankNumber.error = getString(R.string.empty_error)
            status = false
        }else if (!etBankNumber.text.toString().isDigitsOnly()) {
            etBankNumber.error = getString(R.string.invalid_value_error)
            status = false
        }
        return status
    }

    private fun save() {
        if (validateForm()) {
            try {
                viewModel.save(
                    Bank(
                        bId = args.bankId,
                        name = etAccountName.text.toString().trim(),
                        accountNumber = binding.etBankNumber.text.toString().trim().toLong(),
                        img = etBankName.text.toString().trim().plus(".png")
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(),e.toString(), Toast.LENGTH_SHORT).show()
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
        viewModel.bankUpdatedEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                view?.setupSnackbar(it)
            }
        )
    }

    fun exit() {
        findNavController().navigateUp()
    }
}
