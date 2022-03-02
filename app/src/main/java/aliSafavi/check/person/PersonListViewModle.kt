package aliSafavi.check.person

import aliSafavi.check.model.Person
import aliSafavi.check.repository.PersonRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonListViewModle @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {
    suspend fun getAllPersons(): List<Person> {
        return repository.getAllPersons()
    }

}