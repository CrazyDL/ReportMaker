package dlevshtanov.reportmaker.domain

import dlevshtanov.reportmaker.data.db.TableDao
import dlevshtanov.reportmaker.data.db.TitlesDao
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.models.TableEntity
import dlevshtanov.reportmaker.models.TitleEntity
import io.reactivex.Completable
import io.reactivex.Maybe

class MainInteractor(
    private val titlesDao: TitlesDao,
    private val tableDao: TableDao
) {

    fun getTitles() = titlesDao.getAll()

    fun getTable() = tableDao.getAll()

    fun addTitle(item: TitleEntity) = titlesDao.insert(item)

    fun deleteTitle(item: TitleEntity) = titlesDao.delete(item)

    fun updateTitles(items: List<TitleEntity>) = titlesDao.insertAll(items)

    fun deleteFromTable(item: TitleEntity): Completable {
        return if (item.type == Pages.ROW) {
            tableDao.deleteRow(item.title)
        } else {
            tableDao.deleteColumn(item.title)
        }
    }

    fun clearTableData(): Completable {
        return tableDao.clearAll()
    }

    fun clearAllData(): Completable {
        return tableDao.clearAll()
            .andThen(titlesDao.clearAll())
    }

    fun updateTableValue(item: TableEntity): Maybe<TableEntity> {
        return tableDao.getTableValue(item.rowTitle, item.columnTitle)
            .map { oldItem ->
                oldItem.value += item.value
                tableDao.updateAll(oldItem).blockingGet()
                oldItem
            }
    }

    fun addToTableValue(item: TableEntity) = tableDao.insertAll(item)
}