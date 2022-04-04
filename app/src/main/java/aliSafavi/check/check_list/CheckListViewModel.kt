package aliSafavi.check.check_list

import aliSafavi.check.model.FullCheck
import aliSafavi.check.data.repository.CheckRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val repository: CheckRepository
    ) : ViewModel(){

    private val _checks = MutableLiveData<List<FullCheck>>()
    val checks : LiveData<List<FullCheck>>
        get() = _checks

    fun getChecks() {
        viewModelScope.launch {
            repository.getUnPassedChecks().collect {
                _checks.value=it
            }
        }
    }
}