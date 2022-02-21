package aliSafavi.check.repository

import aliSafavi.check.data.FullCheckDao
import aliSafavi.check.model.Check
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckRepository @Inject constructor(
    private val checkDao: FullCheckDao
){

    suspend fun unPaidChecks() = checkDao.getAllUnPaidChecks()
    suspend fun insert(check: Check){
        checkDao.insert(check)
    }
}