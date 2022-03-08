package aliSafavi.check.repository

import aliSafavi.check.data.BankDao
import aliSafavi.check.model.Bank
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankRepository @Inject constructor(
    private val bankDao: BankDao
) {

    suspend fun newBank(bank : Bank){
        bankDao.insert(bank)
    }

    suspend fun update(bank: Bank) {
        bankDao.update(bank)
    }

    suspend fun checkBank(bank: Bank) =
        bankDao.checkBank(accountNum = bank.accountNumber, bankName = bank.name)

    suspend fun getBankbyId(bankId: Int) = bankDao.getBankById(bankId)

    suspend fun getAllBanks() = bankDao.getAllBanks()

}