package aliSafavi.check.check

import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import aliSafavi.check.data.repository.CheckRepository
import aliSafavi.check.data.repository.PersonRepository
import aliSafavi.check.data.repository.BankRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(
    private val checkRepository: CheckRepository,
    private val personRepository : PersonRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private val _check = MediatorLiveData<FullCheck>()
    val check: LiveData<FullCheck>
        get() = _check

    fun initCheck(checkId: Long) {
        viewModelScope.launch {
            _check.value = checkRepository.getCheckById(checkId)
        }
    }

    fun save(checkPrewiew: CheckPrewiew) {
        viewModelScope.launch {
            val bankId = checkRepository.getBankByName(checkPrewiew.bankName).bId
            val personId = checkRepository.getPersonByName(checkPrewiew.personName).pId

            val check = Check(
                cId=checkPrewiew.cId,
                number = checkPrewiew.number,
                amount = checkPrewiew.amount,
                date = checkPrewiew.date,
                personId = personId,
                bankId = bankId
            )

            if (check.cId ==0L)
                checkRepository.insert(check)
            else
                checkRepository.update(check)
        }
    }

    suspend fun getPersonsName(): List<String> {
        return personRepository.getAllPersons().map { it.name }
    }

    suspend fun getBankssName(): List<String> = bankRepository.getBanks().map { it.name }


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