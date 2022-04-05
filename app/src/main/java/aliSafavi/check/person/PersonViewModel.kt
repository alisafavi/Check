package aliSafavi.check.person

import aliSafavi.check.Event
import aliSafavi.check.R
import aliSafavi.check.model.Person
import aliSafavi.check.data.repository.PersonRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    private val _Person = MutableLiveData<Person>()
    val Person: LiveData<Person>
        get() = _Person

    private val _bankUpdatedEvent = MutableLiveData<Event<Int>>()
    val bankUpdatedEvent: LiveData<Event<Int>>
        get() = _bankUpdatedEvent
    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private var personId: Int? = null

    fun start(personId: Int?) {
        this.personId = personId
        if (personId == null) {
            // No need to populate, it's a new bak
            return
        }
        viewModelScope.launch {
            repository.getPerson(personId).let { result ->
                result.onSuccess {
                    _Person.value = it
                }
            }
        }
    }

    fun save(newPerson: Person) {
        if (personId == 0) {
            createPerson(newPerson)
        } else {
            updatePerson(newPerson)
        }
    }

    private fun updatePerson(person: Person) {
        if (personId == 0)
            throw RuntimeException("updateBank() was called but bank is new.")
        viewModelScope.launch {
            repository.updatePerson(person).run {
                onSuccess {
                    _bankUpdatedEvent.value = Event(R.string.successfully_update_person_message)
                    _navigateUp.value = true
                }
                onFailure {
                    _bankUpdatedEvent.value = Event(it.localizedMessage.toInt())
                }
            }
        }
    }

    private fun createPerson(newPerson: Person) = viewModelScope.launch {
        repository.insertPerson(newPerson).run {
            onSuccess {
                _bankUpdatedEvent.value = Event(it)
                _navigateUp.value = true
            }
            onFailure {
                _bankUpdatedEvent.value = Event(it.localizedMessage.toInt())
            }
        }
    }


}