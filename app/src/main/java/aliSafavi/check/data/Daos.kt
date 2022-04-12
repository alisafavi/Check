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
    @Query("select * from `Check` where :isPaid isnull or isPaid =:isPaid order by date")
    fun getFilteredChecks(isPaid :Boolean?): Flow<List<FullCheck>>

    @Query("select * from `check` where cId=:checkId")
    suspend fun getCheck(checkId: Long): FullCheck

    @Query("select * from `check` where number=:checkNumber")
    suspend fun getCheckByNumber(checkNumber: Long): FullCheck

    @Query("select * from `Check` where number=:number")
    suspend fun checkCheck(number: Long): List<Check>

    @Query("update `check` set isPaid=1 where cId=:checkId ")
    suspend fun passCheck(checkId: Long)

    @Query("delete from `check` where cId=:checkId")
    suspend fun deleteCheck(checkId: Long)
}

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)

    @Query("select name from Person")
    suspend fun getPersonsName(): List<String>

    @Query("select * from Person where pId = :id")
    suspend fun getPerson(id: Int): Person

    @Query("select * from Person where name = :name OR phoneNumber = :pNum")
    suspend fun checkPerson(name: String, pNum: Long?): List<Person>

    @Query("select * from Person where name =:personName")
    suspend fun getPersonByName(personName: String): Person

    @Query("select * from Person")
    fun getPersonsObservable(): Flow<List<Person>>
}

@Dao
interface BankDao {
    @Insert
    suspend fun insertBank(bank: Bank)

    @Delete
    suspend fun delete(bank: Bank)

    @Update
    suspend fun update(bank: Bank)

    @Query("select name from Bank")
    suspend fun getBanksName(): List<String>

    @Query("select * from Bank")
    fun getBanksObservable(): Flow<List<Bank>>

    @Query("select * from Bank where accountNumber = :accountNum OR name = :bankName")
    suspend fun checkBank(accountNum: Long, bankName: String): List<Bank>

    @Query("select * from Bank where bId = :bankId")
    suspend fun getBankById(bankId: Int): Bank

    @Query("select * from Bank where name = :bankName")
    suspend fun getBankByName(bankName: String): Bank
}