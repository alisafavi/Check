package aliSafavi.check.repository

import aliSafavi.check.data.BankDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankRepository @Inject constructor(
    private val bankDao: BankDao
) {
}