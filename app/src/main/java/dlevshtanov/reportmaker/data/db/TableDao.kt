package dlevshtanov.reportmaker.data.db

import androidx.room.*
import dlevshtanov.reportmaker.models.TableEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TableDao {

    @Query("SELECT * FROM TableEntity")
    fun getAll(): Single<List<TableEntity>>

    @Query("SELECT * FROM TableEntity WHERE row_title =:rowTitle AND column_title=:columnTitle")
    fun getTableValue(rowTitle: String, columnTitle: String): Maybe<TableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cells: TableEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExist(cells: TableEntity): Completable

    @Update
    fun updateAll(vararg cells: TableEntity): Completable

    @Query("DELETE FROM TableEntity WHERE row_title =:rowTitle")
    fun deleteRow(rowTitle: String): Completable

    @Query("DELETE FROM TableEntity WHERE column_title =:columnTitle")
    fun deleteColumn(columnTitle: String): Completable

    @Delete
    fun delete(cell: TableEntity): Completable

    @Query("DELETE FROM TableEntity")
    fun clearAll(): Completable
}