package aliSafavi.check.di

import aliSafavi.check.data.AppDatabase
import aliSafavi.check.data.BankDao
import aliSafavi.check.data.FullCheckDao
import aliSafavi.check.data.PersonDao
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase{
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideCheckDao(appDatabase: AppDatabase) : FullCheckDao{
        return appDatabase.checkDao()
    }

    @Provides
    fun provideBankDao(appDatabase: AppDatabase) : BankDao{
        return appDatabase.bankDao()
    }

    @Provides
    fun providePersonDao(appDatabase: AppDatabase) : PersonDao{
        return appDatabase.personDao()
    }
}