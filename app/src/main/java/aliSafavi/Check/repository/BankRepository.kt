package aliSafavi.Check.repository

import aliSafavi.Check.data.BankDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankRepository @Inject constructor(
    private val bankDao: BankDao
) {
}