package dlevshtanov.reportmaker.domain

import dlevshtanov.reportmaker.data.db.HistoryDao
import dlevshtanov.reportmaker.data.db.TableDao
import dlevshtanov.reportmaker.data.db.TitlesDao
import dlevshtanov.reportmaker.models.HistoryEntity
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.models.TableEntity
import dlevshtanov.reportmaker.models.TitleEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.*

class MainInteractor(
    private val titlesDao: TitlesDao,
    private val tableDao: TableDao,
    private val historyDao: HistoryDao
) {

    fun getTitles() = titlesDao.getAll()

    fun getTable() = tableDao.getAll()

    fun getHistory() = historyDao.getAll()

    fun addTitle(item: TitleEntity) = titlesDao.insert(item)

    fun addHistory(item: HistoryEntity) = historyDao.insertAll(item)

    fun deleteTitle(item: TitleEntity) = titlesDao.delete(item)

    fun updateTitles(items: List<TitleEntity>) = titlesDao.insertAll(items)

    fun deleteFromTable(item: TitleEntity): Completable {
        return if (item.type == Pages.ROW) {
            tableDao.deleteRow(item.title)
                .andThen(historyDao.deleteRow(item.title))
        } else {
            tableDao.deleteColumn(item.title)
                .andThen(historyDao.deleteColumn(item.title))
        }
    }

    fun clearTableData(): Completable {
        return tableDao.clearAll()
            .andThen(historyDao.clearAll())
    }

    fun clearAllData(): Completable {
        return tableDao.clearAll()
            .andThen(titlesDao.clearAll())
            .andThen(historyDao.clearAll())
    }

    fun resetCell(item: HistoryEntity): Completable {
        return historyDao.resetCell(item.rowTitle, item.columnTitle, item.date)
            .andThen(tableDao.updateAll(TableEntity(item.rowTitle, item.columnTitle, item.wasValue)))
    }

    fun resetAfterDate(item: HistoryEntity): Completable {
        return historyDao.deleteAfterDate(item.date)
            .andThen(tableDao.clearAll())
            .andThen { fillTableFromHistory() }
    }

    fun updateTableValue(item: TableEntity): Maybe<TableEntity> {
        return tableDao.getTableValue(item.rowTitle, item.columnTitle)
            .map { oldItem ->
                val time = Calendar.getInstance().timeInMillis
                historyDao.insertAll(HistoryEntity(time, item.rowTitle, item.columnTitle, item.value, oldItem.value))
                    .blockingGet()
                oldItem.value += item.value
                tableDao.updateAll(oldItem).blockingGet()
                oldItem
            }
    }

    fun addToTableValue(item: TableEntity): Completable {
        val history = HistoryEntity(Calendar.getInstance().timeInMillis, item.rowTitle, item.columnTitle, item.value, 0)
        return tableDao.insertAll(item)
            .andThen(historyDao.insertAll(history))
    }

    private fun fillTableFromHistory(): Completable {
        return historyDao.getAllReversed().flatMapCompletable { history ->
            Completable.fromAction {
                history.forEach {
                    tableDao.insertIfNotExist(TableEntity(it.rowTitle, it.columnTitle, it.wasValue))
                }
            }
        }
    }
}