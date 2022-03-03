package aliSafavi.check.check

import aliSafavi.check.databinding.FragmentCheckBinding
import aliSafavi.check.model.FullCheck
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckFragment : Fragment() {

    private val viewModel: CheckViewModel by viewModels()
    private val args: CheckFragmentArgs by navArgs()

    private lateinit var binding: FragmentCheckBinding

    private lateinit var etCheckNumber: TextInputEditText
    private lateinit var etCheckAmount: TextInputEditText
    private lateinit var etCheckDate: TextInputEditText
    private lateinit var etCheckReciver: MaterialAutoCompleteTextView
    private lateinit var etCheckAccount: MaterialAutoCompleteTextView
    private lateinit var btnSave: Button

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
        etCheckDate = binding.etCheckDate
        etCheckReciver = binding.etCheckReciver
        etCheckAccount = binding.etCheckAccount
        btnSave = binding.btnOk.apply {
            setOnClickListener { save() }
        }
    }

    private fun save() {
        viewModel.save(
            CheckPrewiew(
                cId = args.checkId,
                number = etCheckNumber.text.toString().trim().toLong(),
                date = etCheckDate.text.toString().trim().toLong(),
                amount = etCheckAmount.text.toString().trim().toLong(),
                personName = etCheckReciver.text.toString().trim(),
                bankName = etCheckAccount.text.toString().trim()
            )
        )
    }

}