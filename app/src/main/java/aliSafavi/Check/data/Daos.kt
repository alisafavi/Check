package aliSafavi.Check.data

import aliSafavi.Check.model.Bank
import aliSafavi.Check.model.Check
import aliSafavi.Check.model.Person
import aliSafavi.Check.model.fullCheck
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FullCheckDao{
    @Insert
    suspend fun insert(check: Check)
    @Delete
    suspend fun delete(check: Check)
    @Update
    suspend fun update(check: Check)
    @Transaction
    @Query("select * from `Check` where isPaid = 'false'")
    fun getAllUnPaidChecks(): LiveData<List<fullCheck>>

}

@Dao
interface PersonDao{
    @Insert
    suspend fun insert(person: Person)
    @Delete
    suspend fun delete(person: Person)
    @Update
    suspend fun update(person: Person)
    @Query("select * from Person")
    fun getAllPersons() : Flow<List<Person>>
}

@Dao
interface BankDao{
    @Insert
    suspend fun insert(bank: Bank)
    @Delete
    suspend fun delete(bank: Bank)
    @Update
    suspend fun update(bank: Bank)
    @Query("select * from Bank")
    fun getAllBanks() : Flow<List<Bank>>
}