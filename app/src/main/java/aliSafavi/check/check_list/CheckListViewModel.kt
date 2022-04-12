package aliSafavi.check.check_list

import aliSafavi.check.Event
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

enum class Sort{
    ALL,PAID,UN_PAID
}
@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val repository: CheckRepository
) : ViewModel() {
    private val _checkUpdatedEvent = MutableLiveData<Event<Int>>()
    val checkUpdatedEvent: LiveData<Event<Int>>
        get() = _checkUpdatedEvent

    private val _checks = MutableLiveData<List<FullCheck>>()
    val checks: LiveData<List<FullCheck>>
        get() = _checks

    init {
        filter(Sort.UN_PAID)
    }

    fun passCheck(checkId: Long) {
        viewModelScope.launch {
            repository.passCheck(checkId)
        }
    }

    fun deleteCheck(checkId: Long) {
        viewModelScope.launch { repository.deleteCheck(checkId) }
    }

    fun filter(sort: Sort) {
        val isPaid = when(sort){
            Sort.ALL->null
            Sort.PAID->true
            Sort.UN_PAID->false
        }
        viewModelScope.launch {
            repository.getFilteredChecks(isPaid).collect {
                _checks.value = it
            }
        }
    }
}
