package aliSafavi.check.bank

import aliSafavi.check.data.BankDao
import aliSafavi.check.model.Bank
import aliSafavi.check.repository.BankRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class BankViewModel @Inject constructor(
    private val repository: BankRepository
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack : LiveData<Boolean>
        get() = _navigateBack
    private val _bank = MutableLiveData<Bank>()
    val bank: LiveData<Bank>
        get() = _bank

    fun save_edit(bank: Bank) {
        viewModelScope.launch {
            if (bank.bId == 0) {
                if (repository.checkBank(bank).size == 0) {
                    repository.newBank(bank)
                    _error.value = "new bank created"
                    _navigateBack.value = true
                } else {
                    _error.value = "your bank number or account number is duplicate"
                }
            } else {
                repository.update(bank)
            }
        }
    }

    fun initBank(bankId: Int) {
        if (bankId != 0)
            viewModelScope.launch {
                repository.getBankbyId(bankId)?.let {
                    _bank.value = it
                }
            }
    }

    fun update(bank: Bank){
        viewModelScope.launch {
            repository.update(bank)
            _error.value = "your bank edited"
            _navigateBack.value=true
        }
    }


}