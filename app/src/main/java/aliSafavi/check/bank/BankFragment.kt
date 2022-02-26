package aliSafavi.check.bank

import aliSafavi.check.R
import aliSafavi.check.databinding.FragmentBankBinding
import aliSafavi.check.model.Bank
import android.annotation.SuppressLint
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

    private var argBankId=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        argBankId = args.bankId
        binding = FragmentBankBinding.inflate(inflater,container,false).also {
            it.viewModel = viewModel
            it.lifecycleOwner=this
        }

        initView()
        setupButtons()


        if (argBankId !=0)
            editMode(args.bankId)

        handelMessage()

        return binding.root
    }

    private fun editMode(bankId: Int) {
        viewModel.initBank(bankId)
        btn_save_edit.run {
            text = "edit"
            setBackgroundColor(R.color.purple_200)
            setOnClickListener {
                val nBank = Bank(
                    bId = argBankId,
                    name= bankName.text.toString().trim(),
                    accountNumber = bankNumber.text.toString().trim().toLong(),
                )
                viewModel.update(nBank)
            }
        }
    }
    private fun handelMessage() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it){
                Toast.makeText(requireActivity(),"new bank created",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity,"your bank number or account number is duplicate",Toast.LENGTH_SHORT).show()
//                exit()
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
        binding.lifecycleOwner = this
    }

    fun exit(){
//        editMode(1)
        viewModel.initBank(1)

    }


}