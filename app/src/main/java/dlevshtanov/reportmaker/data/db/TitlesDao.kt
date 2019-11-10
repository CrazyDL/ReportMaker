package dlevshtanov.reportmaker.data.db

import androidx.room.*
import dlevshtanov.reportmaker.models.TitleEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TitlesDao {

    @Query("SELECT * FROM TitleEntity ORDER by order_id")
    fun getAll(): Single<List<TitleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(title: TitleEntity): Maybe<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg titles: TitleEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(titles: List<TitleEntity>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(titles: List<TitleEntity>): Completable

    @Delete
    fun delete(title: TitleEntity): Completable

    @Query("DELETE FROM TitleEntity")
    fun clearAll(): Completable
}