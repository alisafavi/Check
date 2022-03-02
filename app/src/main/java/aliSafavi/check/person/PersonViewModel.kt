package aliSafavi.check.person

import aliSafavi.check.model.Person
import aliSafavi.check.repository.PersonRepository
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

    private val _message = MutableLiveData<String>()
    val message : LiveData<String>
        get() = _message

    private val _Person = MutableLiveData<Person>()
    val Person : LiveData<Person>
        get() = _Person

    fun savePerson(person: Person) {
        viewModelScope.launch {
            if (person.pId == 0) {
                if (repository.checkPerson(person).size ==0){
                    repository.insertPerson(person)
                    _message.value = "New person created"
                }else
                    _message.value = "Person was duplicated"
            } else {
                repository.updatePerson(person)
                _message.value = "person edited"
            }
        }
    }

    fun initPerson(personId: Int) {
        viewModelScope.launch {
            val a = repository.getPersonById(personId)
            _Person.value = a
        }
    }
}