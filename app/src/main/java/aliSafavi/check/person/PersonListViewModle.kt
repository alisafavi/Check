package aliSafavi.check.person

import aliSafavi.check.model.Person
import aliSafavi.check.data.repository.PersonRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModle @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    private val _Persons = MutableLiveData<List<Person>>()
    val persons: LiveData<List<Person>>
        get() = _Persons

    init {
        viewModelScope.launch {
            repository.getPersonsObservable().collect {
                _Persons.value=it
            }
        }
    }

}