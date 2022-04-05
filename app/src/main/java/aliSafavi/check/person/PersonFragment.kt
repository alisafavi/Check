package aliSafavi.check.person

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentPersonBinding
import aliSafavi.check.model.Bank
import aliSafavi.check.model.Person
import aliSafavi.check.utils.setupSnackbar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : Fragment() {

    val viewModel: PersonViewModel by viewModels()
    private lateinit var binding: FragmentPersonBinding

    private lateinit var etPersonName: TextInputEditText
    private lateinit var etPersonPhoneNumber: TextInputEditText
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button

    private val args: PersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        etPersonName = binding.etPersonName
        etPersonPhoneNumber = binding.etPersonPhoneNumber
        btnCancel=binding.btnCancel
        btnOk=binding.btnOk

//        if (args.personId != 0)
//            viewModel.initPerson(args.personId)

        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupButtons()
        setupNavigation()
        setupSnakbar()

        viewModel.start(args.personId)
    }

    private fun setupButtons() {
        btnCancel.setOnClickListener {
            exit()
        }
        btnOk.apply {
            setOnClickListener { save() }
            if (args.personId != 0)
                text = "edit"
        }
    }

    private fun validateForm(): Boolean {
        var status = true
        if (etPersonName.text.toString().isEmpty()) {
            etPersonName.error = getString(R.string.empty_error)
            status = false
        }
        if (!etPersonPhoneNumber.text.toString().isDigitsOnly() && !etPersonPhoneNumber.text.toString().isEmpty()) {
            etPersonPhoneNumber.error = getString(R.string.invalid_value_error)
            status = false
        }
        return status
    }

    private fun save() {
        if (validateForm()) {
            try {
                viewModel.save(
                    Person(
                        pId = args.personId,
                        name = etPersonName.text.toString(),
                        phoneNumber = etPersonPhoneNumber.text.toString().trim()?.let {
                            if (!it.isEmpty())
                                it.toLong()
                            else
                                null
                        }
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
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