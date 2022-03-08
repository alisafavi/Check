package aliSafavi.check.data

import aliSafavi.check.model.Bank
import aliSafavi.check.model.Check
import aliSafavi.check.model.Person
import aliSafavi.check.model.FullCheck
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FullCheckDao {
    @Insert
    suspend fun insert(check: Check)

    @Delete
    suspend fun delete(check: Check)

    @Update
    suspend fun update(check: Check)

    @Transaction
    @Query("select * from `Check` where isPaid = 'false'")
    fun getAllUnPaidChecks(): LiveData<List<FullCheck>>

    @Query("select * from `check` where cId=:checkId")
    suspend fun getById(checkId: Long): FullCheck

}

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)

    @Query("select * from Person")
    suspend fun getAllPersons(): List<Person>

    @Query("select * from Person where pId = :id")
    suspend fun getById(id: Int): Person

    @Query("select * from Person where name = :name OR phoneNumber = :pNum")
    suspend fun checkPerson(name: String, pNum: Long?): List<Person>
    @Query("select * from Person where name =:personName")
    suspend fun getPersonByName(personName: String): Person
}

@Dao
interface BankDao {
    @Insert
    suspend fun insert(bank: Bank)

    @Delete
    suspend fun delete(bank: Bank)

    @Update
    suspend fun update(bank: Bank)

    @Query("select * from Bank")
    suspend fun getAllBanks(): List<Bank>

    @Query("select * from Bank where accountNumber = :accountNum OR name = :bankName")
    suspend fun checkBank(accountNum: Long, bankName: String): List<Bank>

    @Query("select * from Bank where bId = :bankId")
    suspend fun getBankById(bankId: Int): Bank

    @Query("select * from Bank where name = :bankName")
    suspend fun getBankByName(bankName: String): Bank
}