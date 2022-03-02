package aliSafavi.check.repository

import aliSafavi.check.data.PersonDao
import aliSafavi.check.model.Person
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
    suspend fun insertPerson(person: Person){
        personDao.insert(person)
    }

    suspend fun updatePerson(person: Person) {
        personDao.update(person)
    }

    suspend fun checkPerson(person: Person) = personDao.checkPerson(person.name,person.phoneNumber)

    suspend fun getPersonById(id : Int) = personDao.getPersonById(id)

    suspend fun getAllPersons(): List<Person> = personDao.getAllPersons()
}