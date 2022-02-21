package aliSafavi.check.repository

import aliSafavi.check.data.PersonDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
}