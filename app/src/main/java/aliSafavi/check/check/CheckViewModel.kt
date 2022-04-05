package aliSafavi.check.check

import aliSafavi.check.Event
import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import aliSafavi.check.data.repository.CheckRepository
import aliSafavi.check.data.repository.PersonRepository
import aliSafavi.check.data.repository.BankRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(
    private val checkRepository: CheckRepository,
    private val personRepository: PersonRepository,
    private val bankRepository: BankRepository
) : ViewModel() {

    private var checkId: Long? = null
    var date: Long = 0L

    private val _check = MediatorLiveData<FullCheck>()
    val check: LiveData<FullCheck>
        get() = _check


    private val _checkUpdatedEvent = MutableLiveData<Event<Int>>()
    val checkUpdatedEvent: LiveData<Event<Int>> = _checkUpdatedEvent
    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    suspend fun getPersonsName(): List<String> = personRepository.getAllPersons().map { it.name }

    suspend fun getBanksName(): List<String> = bankRepository.getBanks().map { it.name }

    fun start(checkId: Long) {
        this.checkId = checkId
        if (checkId == null) {
            // No need to populate, it's a new bak
            return
        }
        viewModelScope.launch {
            checkRepository.getCheck(checkId).let { result ->
                result.onSuccess {
                    _check.value = it
                    date = it.check.date
                }
            }
        }
    }

    fun save(checkPrewiew: CheckPrewiew) {
        if (checkId == 0L) {
            createCheck(checkPrewiew)
        } else {
            updateCheck(checkPrewiew)
        }
    }

    private suspend fun initCheck(checkPrewiew: CheckPrewiew): Check {
        val personId =
            if (!checkPrewiew.personName.isEmpty())
                checkRepository.getPersonByName(checkPrewiew.personName).pId
            else
                0
        val bankId =
            if (!checkPrewiew.bankName.isEmpty())
                checkRepository.getBankByName(checkPrewiew.bankName).bId
            else
                0

        return Check(
            checkPrewiew.number,
            checkPrewiew.date,
            checkPrewiew.amount,
            checkPrewiew.isPaid,
            personId,
            bankId,
            checkPrewiew.cId
        )
    }

    private fun updateCheck(checkPrewiew: CheckPrewiew) {
        if (checkId == 0L)
            throw RuntimeException("updateCheck() was called but Check is new.")
        viewModelScope.launch {
            val check = initCheck(checkPrewiew)
            checkRepository.updateCheck(check).run {
                onSuccess {
                    _checkUpdatedEvent.value = Event(it)
                    _navigateUp.value = true
                }
                onFailure {
                    _checkUpdatedEvent.value = Event(it.localizedMessage.toInt())
                }
            }
        }
    }

    private fun createCheck(checkPrewiew: CheckPrewiew) = viewModelScope.launch {
        val newCheck = initCheck(checkPrewiew)
        checkRepository.insertCheck(newCheck).run {
            onSuccess {
                _checkUpdatedEvent.value = Event(it)
                _navigateUp.value = true
            }
            onFailure {
                _checkUpdatedEvent.value = Event(it.localizedMessage.toInt())
            }
        }
    }

}

data class CheckPrewiew(
    val cId: Long = 0,
    var number: Long,
    var date: Long,
    var amount: Long,
    val isPaid: Boolean = false,
    var personName: String,
    var bankName: String
)