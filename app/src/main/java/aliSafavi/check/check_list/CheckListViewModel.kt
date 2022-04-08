package aliSafavi.check.check_list

import aliSafavi.check.Event
import aliSafavi.check.model.FullCheck
import aliSafavi.check.data.repository.CheckRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

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
        viewModelScope.launch {
            repository.getUnPassedChecks().collect {
                _checks.value = it
            }
        }
    }
}