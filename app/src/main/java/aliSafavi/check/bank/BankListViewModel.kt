package aliSafavi.check.bank

import aliSafavi.check.model.Bank
import aliSafavi.check.repository.BankRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankListViewModel @Inject constructor(
    private val repository: BankRepository
) : ViewModel() {

    val _banks = MutableLiveData<List<Bank>>()
    val banks: LiveData<List<Bank>>
        get() = _banks

    init {
        viewModelScope.launch {
            _banks.value = repository.getAllBanks()
        }
    }


}