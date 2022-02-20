package aliSafavi.Check.check

import aliSafavi.Check.model.Check
import aliSafavi.Check.repository.CheckRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(
    private val repository: CheckRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun inser(check: Check){
        viewModelScope.launch {
            repository.insert(check)
        }
    }

}