package aliSafavi.check.repository

import aliSafavi.check.data.BankDao
import aliSafavi.check.data.FullCheckDao
import aliSafavi.check.data.PersonDao
import aliSafavi.check.model.Bank
import aliSafavi.check.model.Check
import aliSafavi.check.model.FullCheck
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckRepository @Inject constructor(
    private val checkDao: FullCheckDao,
    private val bankDao: BankDao,
    private val personDao: PersonDao
) {

    suspend fun unPaidChecks() = checkDao.getAllUnPaidChecks()
    suspend fun insert(check: Check) {
        checkDao.insert(check)
    }

    suspend fun getCheckById(checkId: Long): FullCheck = checkDao.getById(checkId)
    suspend fun getBankByName(bankName: String) = bankDao.getBankByName(bankName)
    suspend fun getPersonByName(personName: String) = personDao.getPersonByName(personName)
    suspend fun update(check: Check) {
        checkDao.update(check)
    }

}