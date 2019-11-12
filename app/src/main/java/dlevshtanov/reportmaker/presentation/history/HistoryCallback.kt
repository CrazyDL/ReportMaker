package dlevshtanov.reportmaker.presentation.history

import dlevshtanov.reportmaker.models.HistoryEntity

interface HistoryCallback {

    fun onItemClicked(item: HistoryEntity)

}