package aliSafavi.Check.repository

import aliSafavi.Check.data.FullCheckDao
import aliSafavi.Check.model.Check
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