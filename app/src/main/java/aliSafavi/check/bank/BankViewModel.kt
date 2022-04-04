package aliSafavi.check.bank

import aliSafavi.check.Event
import aliSafavi.check.R
import aliSafavi.check.model.Bank
import aliSafavi.check.repository.BankRepository
import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class BankViewModel @Inject constructor(
    private val repository: BankRepository
) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    private val _bank = MutableLiveData<Bank>()
    val bank: LiveData<Bank>
        get() = _bank
    private val _bankUpdatedEvent = MutableLiveData<Event<Int>>()
    val bankUpdatedEvent: LiveData<Event<Int>> = _bankUpdatedEvent
    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp


    private var bankId: Int? = null

    fun start(bankId: Int?) {
        this.bankId = bankId
        if (bankId == null) {
            // No need to populate, it's a new bak
            return
        }
        viewModelScope.launch {
            repository.getBank(bankId).let { result ->
                result.onSuccess {
                    _bank.value = it
                }
            }
        }
    }

    fun save(newbank: Bank) {
        if (bankId == 0) {
            createCheck(Bank(newbank.name, newbank.accountNumber, newbank.img))
        } else {
            updateBank(Bank(newbank.name, newbank.accountNumber, newbank.img, bankId!!))
        }

    }

    private fun updateBank(bank: Bank) {
        if (bank.bId == null)
            throw RuntimeException("updateBank() was called but bank is new.")
        viewModelScope.launch {
            repository.saveBank(bank)
            _bankUpdatedEvent.value = Event(R.string.successfully_update_bank_message)
            _navigateUp.value=true
        }
    }

    private fun createCheck(newBank: Bank) = viewModelScope.launch {
        repository.saveBank(newBank).run {
            onSuccess {
                _bankUpdatedEvent.value = Event(it)
                _navigateUp.value=true
            }
            onFailure {
                _bankUpdatedEvent.value = Event(it.localizedMessage.toInt())
            }
        }

    }
}