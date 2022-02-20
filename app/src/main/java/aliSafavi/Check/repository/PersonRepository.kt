package aliSafavi.Check.repository

import aliSafavi.Check.data.PersonDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
}