package aliSafavi.check.person

import aliSafavi.check.databinding.FragmentPersonBinding
import aliSafavi.check.model.Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        if (args.personId != 0)
            viewModel.initPerson(args.personId)

        binding.btnOk.setOnClickListener {
            viewModel.savePerson(
                Person(
                    pId = args.personId,
                    name = etPersonName.text.toString().trim(),
                    phoneNumber = etPersonPhoneNumber.text.toString().trim()?.toLong()
                )
            )
        }

        binding.btnCancel.setOnClickListener {
            navigateUp()
        }

        handelMessage()

        viewModel.Person.observe(viewLifecycleOwner, Observer {
            etPersonName.setText(it.name)
        })

        binding.lifecycleOwner = this
        return binding.root
    }
    
    fun handelMessage(){
        viewModel.message.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root,it,Snackbar.LENGTH_LONG).show()
            navigateUp()
        })
    }


    fun navigateUp(){
        findNavController().navigateUp()
    }

}