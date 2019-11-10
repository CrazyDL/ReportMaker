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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val fragmentsViewModel: FragmentsViewModel,
    private val mainInteractor: MainInteractor
) : ViewModel() {

    val fabVisibilityLiveData = MutableLiveData<Boolean>()
    val toolbarTitleLiveData = MutableLiveData<String?>()
    val showAddTitleDialogLiveData = MutableLiveData<String>()
    val cellValueLiveData = MutableLiveData(1)
    val currentPageLiveData = MutableLiveData<Pages>()
    val alreadyExistsAlertLiveData = MutableLiveData<Pages>()
    private val compositeDisposable = CompositeDisposable()
    private val updateTableSubject = PublishSubject.create<List<TitleEntity>>()

    private var currentPage = Pages.ROW
    private var currentItem: TitleEntity? = null

    fun initViewModel(page: Pages) {
        currentPage = page
        initItems(true)
        registerInitSubject()
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
                .subscribe({ fragmentsViewModel.clearTable() }, { ex -> Log.e(TAG, ex.message, ex) })
        )
    }

    fun onClearAllDataClicked() {
        resetSelectedItem()
        compositeDisposable.add(
            mainInteractor.clearAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ fragmentsViewModel.clearAll() }, { ex -> Log.e(TAG, ex.message, ex) })
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
        if (title.trim().isNotEmpty()) {
            val item = TitleEntity(type = currentPage, title = title.trim())
            compositeDisposable.add(
                mainInteractor.addTitle(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ id ->
                        if (id != -1L) {
                            fragmentsViewModel.addItem(item)
                        } else {
                            alreadyExistsAlertLiveData.value = currentPage
                        }
                    }, { ex -> Log.e(TAG, ex.message, ex) })
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
                        Log.e(TAG, ex.message, ex)
                    }, {
                        onCellAdded(cell)
                        mainInteractor.addToTableValue(cell)
                            .subscribeOn(Schedulers.io())
                            .subscribe({}, { ex -> Log.e(TAG, ex.message, ex) })
                    })
            )
        }
    }

    fun deleteItem(item: TitleEntity) {
        compositeDisposable.add(
            mainInteractor.deleteTitle(item)
                .andThen(mainInteractor.deleteFromTable(item))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateTableSubject.onNext(emptyList()) }, { ex -> Log.e(TAG, ex.message, ex) })
        )
    }

    fun updateItems(items: List<TitleEntity>) {
        updateTableSubject.onNext(items)
    }

    fun onFabClicked(context: Context) {
        showAddTitleDialogLiveData.value = context.getString(
            if (currentPage == Pages.ROW) R.string.add_row_dialog_title else R.string.add_column_dialog_title
        )
    }

    private fun registerInitSubject() {
        compositeDisposable.add(
            updateTableSubject
                .throttleLatest(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            initItems(false)
                        } else {
                            mainInteractor.updateTitles(it).subscribe({ initItems(false) }, {})
                        }
                    },
                    { ex -> Log.e(TAG, ex.message, ex) })
        )
    }

    private fun initItems(needUpdateTitles: Boolean) {
        compositeDisposable.add(
            mainInteractor.getTitles()
                .zipWith(
                    mainInteractor.getTable(),
                    BiFunction { titles: List<TitleEntity>, table: List<TableEntity> -> Pair(titles, table) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { items -> fragmentsViewModel.initItems(items.first, items.second, needUpdateTitles) },
                    { ex -> Log.e(TAG, ex.message, ex) })
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

    companion object {
        private const val TAG = "MainViewModel"
    }
}