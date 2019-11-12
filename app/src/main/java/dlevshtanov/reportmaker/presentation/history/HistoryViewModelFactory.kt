package dlevshtanov.reportmaker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dlevshtanov.reportmaker.DependenciesProvider

class HistoryViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HistoryViewModel(DependenciesProvider.getMainInteractor()) as T
    }
}