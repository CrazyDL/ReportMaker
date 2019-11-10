package dlevshtanov.reportmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dlevshtanov.reportmaker.data.db.AppDatabase.Companion.DATABASE_VERSION
import dlevshtanov.reportmaker.models.TableEntity
import dlevshtanov.reportmaker.models.TitleEntity


@Database(entities = [TitleEntity::class, TableEntity::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(PagesConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun titlesDao(): TitlesDao
    abstract fun tableDao(): TableDao

    companion object {
        const val DATABASE_NAME = "reports.db"
        const val DATABASE_VERSION = 2
    }
}