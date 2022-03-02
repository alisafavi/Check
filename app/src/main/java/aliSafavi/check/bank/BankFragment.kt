package aliSafavi.check.bank

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentBankBinding
import aliSafavi.check.model.Bank
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

        binding = FragmentBankBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        initView()
        setupButtons()


        if (args.bankId != 0)
            editMode(args.bankId)

        handelMessage()

        return binding.root
    }

    private fun editMode(bankId: Int) {
        viewModel.initBank(bankId)
        btnSaveEdit.run {
            text = "edit"
            setBackgroundColor(R.color.purple_200)
            setOnClickListener {
                viewModel.update(
                    Bank(
                        bId = args.bankId,
                        name = etAccountName.text.toString().trim(),
                        accountNumber = etBankNumber.text.toString().trim().toLong(),
                        img = etBankName.text.toString() + ".png"
                    )
                )
            }
        }
    }

    private fun handelMessage() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                it,
                Snackbar.LENGTH_LONG
            ).show()
        })
    }

    private fun setupButtons() {
        btnCancel.setOnClickListener {
            exit()
        }
        btnSaveEdit.setOnClickListener {
            viewModel.save_edit(
                Bank(
                    name = etAccountName.text.toString(),
                    accountNumber = binding.etBankNumber.text.toString().toLong(),
                    img = etBankName.text.toString().trim().plus(".png")
                )
            )
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
            }
            showDropDown()
        }

        etAccountName = binding.etAccountName
        etBankNumber = binding.etBankNumber
        btnCancel = binding.btnCancel
        btnSaveEdit = binding.btnSaveEdit
        binding.lifecycleOwner = this

        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            if (it)
                exit()
        })
    }

    private fun exit() {
        findNavController().navigateUp()
    }

}
