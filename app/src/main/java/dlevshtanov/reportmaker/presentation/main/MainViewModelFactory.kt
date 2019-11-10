package dlevshtanov.reportmaker.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dlevshtanov.reportmaker.DependenciesProvider
import dlevshtanov.reportmaker.presentation.FragmentsViewModel

class MainViewModelFactory(private val fragmentsViewModel: FragmentsViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(fragmentsViewModel, DependenciesProvider.getMainInteractor()) as T
    }
}