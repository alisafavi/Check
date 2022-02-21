package aliSafavi.check.check

import aliSafavi.check.model.Check
import aliSafavi.check.repository.CheckRepository
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