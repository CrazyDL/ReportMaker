package dlevshtanov.reportmaker.data.db

import androidx.room.*
import dlevshtanov.reportmaker.models.HistoryEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity ORDER BY date")
    fun getAll(): Single<List<HistoryEntity>>

    @Query("SELECT * FROM HistoryEntity ORDER BY date DESC")
    fun getAllReversed(): Single<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cells: HistoryEntity): Completable

    @Query("DELETE FROM HistoryEntity WHERE row_title =:rowTitle AND column_title =:columnTitle AND date >=:date")
    fun resetCell(rowTitle: String, columnTitle: String, date: Long): Completable

    @Query("DELETE FROM HistoryEntity WHERE date >=:date")
    fun deleteAfterDate(date: Long): Completable

    @Query("DELETE FROM HistoryEntity WHERE row_title =:rowTitle")
    fun deleteRow(rowTitle: String): Completable

    @Query("DELETE FROM HistoryEntity WHERE column_title =:columnTitle")
    fun deleteColumn(columnTitle: String): Completable

    @Delete
    fun delete(history: HistoryEntity): Completable

    @Query("DELETE FROM HistoryEntity")
    fun clearAll(): Completable
}