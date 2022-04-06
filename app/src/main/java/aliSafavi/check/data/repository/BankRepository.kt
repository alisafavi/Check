package aliSafavi.check.data.repository

import aliSafavi.check.R
import aliSafavi.check.data.BankDao
import aliSafavi.check.model.Bank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankRepository @Inject constructor(
    private val bankDao: BankDao
) {
    suspend fun getBanksName() = bankDao.getBanksName()

    fun getBanksObservable() = bankDao.getBanksObservable()

    suspend fun insertBank(newBank: Bank): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val checking = bankDao.checkBank(newBank.accountNumber, newBank.name)
            if (checking.isEmpty() && newBank.bId == 0) {
                bankDao.insertBank(newBank)
                return@withContext Result.success(R.string.successfully_saved_bank_message)
            } else
                return@withContext Result.failure(AssertionError(R.string.duplicate_bank_message))
        } catch (e: java.lang.Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun updateBank(newBank: Bank): Result<Int> = withContext(Dispatchers.IO) {
        try {
            bankDao.update(newBank)
            return@withContext Result.success(R.string.successfully_update_bank_message)
        } catch (e: Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun getBank(bankId: Int): Result<Bank> = withContext(Dispatchers.IO) {
        val result = bankDao.getBankById(bankId)
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

