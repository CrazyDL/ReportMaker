package dlevshtanov.reportmaker.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.presentation.table.TableFragment
import dlevshtanov.reportmaker.presentation.titles.ColumnFragment
import dlevshtanov.reportmaker.presentation.titles.RowFragment

class MainPagerAdapter(
    private val context: FragmentActivity
) : FragmentStateAdapter(context) {


    override fun getItemCount(): Int {
        return Pages.values().size - 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Pages.ROW.position -> RowFragment.newInstance()
            Pages.COLUMN.position -> ColumnFragment.newInstance()
            Pages.TABLE.position -> TableFragment.newInstance()
            else -> throw IllegalStateException("Wrong pager position")
        }
    }

    fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            Pages.ROW.position -> context.getString(R.string.row)
            Pages.COLUMN.position -> context.getString(R.string.column)
            Pages.TABLE.position -> context.getString(R.string.table)
            else -> throw IllegalStateException("Wrong pager position")
        }
    }
}