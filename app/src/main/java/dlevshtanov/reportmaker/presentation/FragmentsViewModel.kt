package dlevshtanov.reportmaker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.models.TableEntity
import dlevshtanov.reportmaker.models.TitleEntity

class FragmentsViewModel : ViewModel() {

    val addRowLiveData = MutableLiveData<TitleEntity>()
    val initRowsLiveData = MutableLiveData<List<TitleEntity>>()
    val addColumnLiveData = MutableLiveData<TitleEntity>()
    val initColumnsLiveData = MutableLiveData<List<TitleEntity>>()
    val updateCellLiveData = MutableLiveData<TableEntity>()
    val clearCellsLiveData = MutableLiveData<Void>()
    val initTableLiveData = MutableLiveData<Triple<List<String>, List<String>, List<List<Int>>>>()

    fun updateCell(item: TableEntity) {
        updateCellLiveData.value = item
    }

    fun addItem(item: TitleEntity) {
        if (item.type == Pages.ROW) {
            addRowLiveData.value = item
        } else if (item.type == Pages.COLUMN) {
            addColumnLiveData.value = item
        }
    }

    fun initItems(titles: List<TitleEntity>, items: List<TableEntity>) {
        if (titles.isNotEmpty()) {
            val rowTitles = titles.filter { it.type == Pages.ROW }
            val columnTitles = titles.filter { it.type == Pages.COLUMN }
            initRowsLiveData.value = rowTitles
            initColumnsLiveData.value = columnTitles
            val rowStrings = rowTitles.map { it.title }
            val columnStrings = columnTitles.map { it.title }
            val cells = MutableList(rowTitles.size) { MutableList(columnTitles.size) { 0 } }
            items.forEach { item ->
                val rowInd = rowStrings.indexOf(item.rowTitle)
                val columnInd = columnStrings.indexOf(item.columnTitle)
                cells[rowInd][columnInd] = item.value
            }
            initTableLiveData.value = Triple(columnStrings, rowStrings, cells)
        }
    }

    fun clearTable() {
        clearCellsLiveData.value = null
    }

    fun clearAll() {
        initTableLiveData.value = Triple(emptyList(), emptyList(), emptyList())
        initRowsLiveData.value = emptyList()
        initColumnsLiveData.value = emptyList()
    }
}