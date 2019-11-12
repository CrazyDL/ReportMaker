package dlevshtanov.reportmaker

import android.app.Application
import androidx.room.Room
import dlevshtanov.reportmaker.data.db.AppDatabase
import dlevshtanov.reportmaker.domain.MainInteractor


class DependenciesProvider : Application() {


    override fun onCreate() {
        super.onCreate()
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
        mainInteractor = MainInteractor(appDatabase.titlesDao(), appDatabase.tableDao(), appDatabase.historyDao())
    }

    companion object {
        private lateinit var appDatabase: AppDatabase
        private lateinit var mainInteractor: MainInteractor

        fun getMainInteractor() = mainInteractor
    }
}