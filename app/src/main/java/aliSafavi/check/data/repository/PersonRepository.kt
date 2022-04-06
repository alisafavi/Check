package aliSafavi.check.data.repository

import aliSafavi.check.R
import aliSafavi.check.data.PersonDao
import aliSafavi.check.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {

    suspend fun getPerson(personId: Int): Result<Person> = withContext(Dispatchers.IO) {
        val result = personDao.getPerson(personId)
        try {
            if (result != null) {
                return@withContext Result.success(result)
            } else
                return@withContext Result.failure(Exception("Task not found!"))
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun insertPerson(newPerson: Person): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val checking = personDao.checkPerson(newPerson.name, newPerson.phoneNumber)
            if (checking.isEmpty() && newPerson.pId == 0) {
                personDao.insert(newPerson)
                return@withContext Result.success(R.string.successfully_saved_person_message)
            } else
                return@withContext Result.failure(AssertionError(R.string.duplicate_person_message))
        } catch (e: Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun updatePerson(newPerson: Person): Result<Int> = withContext(Dispatchers.IO) {
        try {
            personDao.update(newPerson)
            return@withContext Result.success(R.string.successfully_saved_person_message)
        } catch (e: Exception) {
            return@withContext Result.failure(AssertionError(R.string.erroe_message))
        }
    }

    suspend fun getPersonsName(): List<String> = personDao.getPersonsName()
    fun getPersonsObservable() = personDao.getPersonsObservable()
}