package aliSafavi.check.check

import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import aliSafavi.check.repository.CheckRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(
    private val repository: CheckRepository,
) : ViewModel() {

    private val _check = MediatorLiveData<FullCheck>()
    val check: LiveData<FullCheck>
        get() = _check

    fun initCheck(checkId: Long) {
        viewModelScope.launch {
            _check.value = repository.getCheckById(checkId)
        }
    }

    fun save(checkPrewiew: CheckPrewiew) {
        viewModelScope.launch {
            val bankId = repository.getBankByName(checkPrewiew.bankName).bId
            val personId = repository.getPersonByName(checkPrewiew.personName).pId

            val check = Check(
                cId=checkPrewiew.cId,
                number = checkPrewiew.number,
                amount = checkPrewiew.amount,
                date = 111111L,
                personId = personId,
                bankId = bankId
            )

            if (check.cId ==0L)
                repository.insert(check)
            else
                repository.update(check)
        }
    }


}

data class CheckPrewiew(
    val cId: Long = 0,
    var number: Long?,
    var date: Long,
    var amount: Long,
    val isPaid: Boolean = false,
    var personName: String,
    var bankName: String
)