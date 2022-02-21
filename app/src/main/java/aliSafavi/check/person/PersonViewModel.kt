package aliSafavi.check.person

import aliSafavi.check.repository.PersonRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {
}