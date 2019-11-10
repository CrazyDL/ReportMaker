package dlevshtanov.reportmaker.presentation.titles

import dlevshtanov.reportmaker.models.TitleEntity

interface TitlesCallback {

    fun onItemClicked(item: TitleEntity)

    fun onItemDeleted(item: TitleEntity)

    fun onItemsChanged(items: List<TitleEntity>)
}