package dlevshtanov.reportmaker.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.domain.MainInteractor
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.models.TableEntity
import dlevshtanov.reportmaker.models.TitleEntity
import dlevshtanov.reportmaker.presentation.FragmentsViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val fragmentsViewModel: FragmentsViewModel,
    private val mainInteractor: MainInteractor
) : ViewModel() {

    val fabVisibilityLiveData = MutableLiveData<Boolean>()
    val toolbarTitleLiveData = MutableLiveData<String?>()
    val showAddTitleDialogLiveData = MutableLiveData<String>()
    val cellValueLiveData = MutableLiveData(1)
    val currentPageLiveData = MutableLiveData<Pages>()
    private val compositeDisposable = CompositeDisposable()

    private var currentPage = Pages.ROW
    private var currentItem: TitleEntity? = null

    fun initViewModel(page: Pages) {
        currentPage = page
        compositeDisposable.add(
            mainInteractor.getTitles()
                .zipWith(
                    mainInteractor.getTable(),
                    BiFunction { titles: List<TitleEntity>, table: List<TableEntity> -> Pair(titles, table) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items -> fragmentsViewModel.initItems(items.first, items.second) }, {})
        )
    }

    fun onDecreaseClick() {
        cellValueLiveData.value = cellValueLiveData.value?.minus(1)
    }

    fun onIncreaseClick() {
        cellValueLiveData.value = cellValueLiveData.value?.plus(1)
    }

    fun resetSelectedItem() {
        currentItem = null
        toolbarTitleLiveData.value = null
    }

    fun onClearTableDataClicked() {
        resetSelectedItem()
        compositeDisposable.add(
            mainInteractor.clearTableData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ fragmentsViewModel.clearTable() }, {})
        )
    }

    fun onClearAllDataClicked() {
        resetSelectedItem()
        compositeDisposable.add(
            mainInteractor.clearAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ fragmentsViewModel.clearAll() }, {})
        )
    }

    fun onShowHistoryClicked() {
        resetSelectedItem()
    }

    fun onPageSelected(page: Pages) {
        currentPage = page
        fabVisibilityLiveData.value = page == Pages.ROW || page == Pages.COLUMN
        if (currentItem == null) {
            toolbarTitleLiveData.value = null
        }
    }

    fun onTitleAdded(title: String) {
        if (title.isNotEmpty()) {
            val item = TitleEntity(type = currentPage, title = title)
            compositeDisposable.add(
                mainInteractor.addTitle(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ id ->
                        if (id != -1L) {
                            fragmentsViewModel.addItem(item)
                        }
                    }, {})
            )

        }

    }

    fun onItemClicked(item: TitleEntity) {
        if (currentItem == null || item.type == currentItem?.type) {
            val titleBegin = if (currentPage == Pages.ROW) "Строка" else "Столбец"
            toolbarTitleLiveData.value = "$titleBegin (${item.title})"
            currentItem = item
            currentPageLiveData.value = if (currentPage == Pages.ROW) Pages.COLUMN else Pages.ROW
        } else {
            val rowTitle = if (item.type == Pages.ROW) item.title else currentItem?.title.orEmpty()
            val columnTitle = if (item.type == Pages.COLUMN) item.title else currentItem?.title.orEmpty()
            val cell = TableEntity(0, rowTitle, columnTitle, cellValueLiveData.value ?: 0)
            compositeDisposable.add(
                mainInteractor.updateTableValue(cell)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        onCellAdded(it)
                    }, { ex ->
                        Log.e("MainViewModel", ex.message, ex)
                    }, {
                        onCellAdded(cell)
                        mainInteractor.addToTableValue(cell).subscribe({}, {})
                    })
            )
        }
    }

    fun deleteItem(item: TitleEntity) {
        mainInteractor.deleteTitle(item).execute()
    }

    fun updateItems(items: List<TitleEntity>) {
        mainInteractor.updateTitles(items).execute()
    }

    fun onFabClicked(context: Context) {
        showAddTitleDialogLiveData.value = context.getString(
            if (currentPage == Pages.ROW) R.string.add_row_dialog_title else R.string.add_column_dialog_title
        )
    }

    private fun onCellAdded(tableEntity: TableEntity) {
        currentItem = null
        currentPageLiveData.value = if (currentPage == Pages.ROW) Pages.COLUMN else Pages.ROW
        fragmentsViewModel.updateCell(tableEntity)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun Completable.execute() {
        compositeDisposable.add(this.subscribeOn(Schedulers.io()).subscribe({}, {}))
    }
}