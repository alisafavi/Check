package aliSafavi.check.check_list

import aliSafavi.check.repository.CheckRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val repository: CheckRepository
) : ViewModel(){
}