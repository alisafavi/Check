package aliSafavi.check.data.repository

import aliSafavi.check.R
import aliSafavi.check.data.BankDao
import aliSafavi.check.data.FullCheckDao
import aliSafavi.check.data.PersonDao
import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckRepository @Inject constructor(
    private val checkDao: FullCheckDao,
    private val bankDao: BankDao,
    private val personDao: PersonDao
) {

    fun getUnPassedChecks() = checkDao.getUnPassedChecks()
    suspend fun insert(check: Check) {
        checkDao.insert(check)
    }

    suspend fun getBankByName(bankName: String) = with(Dispatchers.IO) {
        bankDao.getBankByName(bankName)
    }

    suspend fun getPersonByName(personName: String) = with(Dispatchers.IO) {
        personDao.getPersonByName(personName)
    }

    suspend fun update(check: Check) {
        checkDao.update(check)
    }


    suspend fun insertCheck(newCheck: Check): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val checking = checkDao.checkCheck(newCheck.number)
            if (checking.isEmpty() && newCheck.cId == 0L) {
                checkDao.insert(newCheck)
                return@withContext Result.success(R.string.successfully_saved_check_message)
            } else
                return@withContext Result.failure(AssertionError(R.string.duplicate_check_message))
        } catch (e: Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun updateCheck(newCheck: Check): Result<Int> = withContext(Dispatchers.IO) {
        try {
            checkDao.update(newCheck)
            return@withContext Result.success(R.string.successfully_update_check_message)
        } catch (e: Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun getCheck(CheckId: Long): Result<FullCheck> = withContext(Dispatchers.IO) {
        val result = checkDao.getCheck(CheckId)
        try {
            if (result != null) {
                return@withContext Result.success(result)
            } else
                return@withContext Result.failure(Exception("Task not found!"))
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }


}