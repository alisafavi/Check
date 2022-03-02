package aliSafavi.check.data

import aliSafavi.check.model.Bank
import aliSafavi.check.model.Check
import aliSafavi.check.model.Person
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Check::class, Person::class, Bank::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun checkDao() : FullCheckDao
    abstract fun personDao() : PersonDao
    abstract fun bankDao() : BankDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "check")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}