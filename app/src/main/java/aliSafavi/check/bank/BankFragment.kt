package aliSafavi.check.bank

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentBankBinding
import aliSafavi.check.model.Bank
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankFragment : Fragment() {

    private lateinit var binding : FragmentBankBinding
    private val viewModel : BankViewModel by viewModels()
    private val args : BankFragmentArgs by navArgs()

    private lateinit var bankImg : ShapeableImageView
    private lateinit var bankName : TextInputEditText
    private lateinit var bankNumber : TextInputEditText
    private lateinit var btn_cancel : Button
    private lateinit var btn_save_edit : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBankBinding.inflate(inflater,container,false)

        initView()
        setupButtons()

        args.bankId?.let {
            editMode(it)
        }
        handelMessage()

        return binding.root
    }

    private fun editMode(bankId: Int) {
//        TODO("I should continue from here")
        val oldBank = viewModel.getBankById(bankId)

        btn_save_edit.apply {
            text = "edit"
            setBackgroundColor(Color.BLUE)
//            setOnClickListener {
//                val bankName = bankName.text.toString()
//                val bankNumber = bankNumber.text.toString().toLong()
//                val bank = Bank(bankId,bankName,bankNumber,oldBank.)
//                viewModel.update()
//            }
        }
    }
    private fun handelMessage() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it){
                Toast.makeText(requireActivity(),"new bank created",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity,"your bank number or account number is duplicate",Toast.LENGTH_SHORT).show()
                exit()
            }
        })
    }

    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            exit()
        }
        binding.btnSaveEdit.setOnClickListener {
            viewModel.save_edit(Bank(name = binding.etBankName.text.toString(), accountNumber = binding.etBankNumber.text.toString().toLong()))
        }
    }

    private fun initView() {
        bankImg = binding.bankImg.apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                .build()
        }
        bankName = binding.etBankName
        bankNumber = binding.etBankNumber
        btn_cancel = binding.btnCancel
        btn_save_edit = binding.btnSaveEdit
    }

    fun exit(){
//        Na
    }


}