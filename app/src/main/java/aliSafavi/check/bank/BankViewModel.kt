package aliSafavi.check.bank

import aliSafavi.check.model.Bank
import aliSafavi.check.repository.BankRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class BankViewModel @Inject constructor(
    private val repository: BankRepository
) : ViewModel(){

    val error = MutableLiveData<Boolean>()

    fun save_edit(bank: Bank) {
        viewModelScope.launch {
            if (bank.bId == 0){
                if(repository.checkBank(bank).size == 0){
                    repository.newBank(bank)
                    error.value = true
                }
                else{
                    error.value = false
                }
            }else{
                repository.update(bank)
            }
        }
    }

    fun getBankById(bankId: Int) {
//        viewModelScope.launch {
//            repository.getBankbyId(bankId)
//
//        }
    }


}