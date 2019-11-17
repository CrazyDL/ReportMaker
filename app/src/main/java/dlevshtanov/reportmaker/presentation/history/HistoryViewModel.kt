package dlevshtanov.reportmaker.presentation.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dlevshtanov.reportmaker.domain.MainInteractor
import dlevshtanov.reportmaker.models.HistoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HistoryViewModel(
    private val mainInteractor: MainInteractor
) : ViewModel() {

    val initHistoryLiveData = MutableLiveData<List<HistoryEntity>>()
    var currentItem: HistoryEntity? = null
    private val compositeDisposable = CompositeDisposable()

    fun initItems() {
        compositeDisposable.add(
            mainInteractor.getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ initHistoryLiveData.value = it }, { ex -> Log.e(TAG, ex.message, ex) })
        )
    }

    fun onResetCellClicked() {
        currentItem?.let {
            compositeDisposable.add(
                mainInteractor.resetCell(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ initItems() }, { ex -> Log.e(TAG, ex.message, ex) })
            )
        }
    }

    fun onResetAfterDateClicked() {
        currentItem?.let {
            compositeDisposable.add(
                mainInteractor.resetAfterDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ initItems() }, { ex -> Log.e(TAG, ex.message, ex) })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}