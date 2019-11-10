/*
package com.cleveroad.adaptivetablelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.TableEntity


class TableAdapter(context: Context) : LinkedAdaptiveTableAdapter<ViewHolderImpl>() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val columnWidth: Int = context.resources.getDimensionPixelSize(R.dimen.column_width)
    private var rowHeight: Int = context.resources.getDimensionPixelSize(R.dimen.row_height)
    private var headerHeight: Int = context.resources.getDimensionPixelSize(R.dimen.column_header_height)
    private var headerWidth: Int = context.resources.getDimensionPixelSize(R.dimen.row_header_width)
    private var data: HashMap<String, HashMap<String, Int>> = HashMap()
    private val rows: MutableList<String> = mutableListOf("Строки", "a")
    private val columns: MutableList<String> = mutableListOf("Столбцы", "b")

    fun updateCell(item: TableEntity) {
        data[item.rowTitle]?.put(item.columnTitle, item.value)
        notifyItemChanged(rows.indexOf(item.rowTitle), columns.indexOf(item.columnTitle))
    }

    fun initTable(rowTitles: List<String>, columnTitles: List<String>, items: List<TableEntity>) {
        rows.clear()
        rows.addAll(rowTitles)
        columns.clear()
        columns.addAll(columnTitles)
        items.forEach {
            val tableRows = data[it.rowTitle]
            if (tableRows != null) {
                data[it.rowTitle]?.put(it.columnTitle, it.value)
            } else {
                val tableColumn = HashMap<String, Int>()
                tableColumn[it.columnTitle] = it.value
                data[it.rowTitle] = tableColumn
            }
        }
    }

    override fun getRowCount(): Int {
        return if (rows.isEmpty()) 1 else rows.size
    }

    override fun getColumnCount(): Int {
        return if (columns.isEmpty()) 1 else columns.size
    }

    override fun onCreateItemViewHolder(parent: ViewGroup): ViewHolderImpl {
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_cell, parent, false))
    }

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup): ViewHolderImpl {
        return HeaderColumnViewHolder(layoutInflater.inflate(R.layout.item_header_column, parent, false))
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup): ViewHolderImpl {
        return HeaderRowViewHolder(layoutInflater.inflate(R.layout.item_header_row, parent, false))
    }

    override fun onCreateLeftTopHeaderViewHolder(parent: ViewGroup): ViewHolderImpl {
        return HeaderLeftTopViewHolder(layoutInflater.inflate(R.layout.item_header_left_top, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolderImpl, row: Int, column: Int) {
        val vh = viewHolder as ItemViewHolder
        val itemData = data[rows[row - 1]]?.get(columns[column - 1]) ?: 0

        vh.itemTextView.visibility = View.VISIBLE
        vh.itemTextView.text = itemData.toString()
    }

    override fun onBindHeaderColumnViewHolder(viewHolder: ViewHolderImpl, column: Int) {
        val vh = viewHolder as HeaderColumnViewHolder
        vh.headerColumnTextView.text = columns[column]

    }

    override fun onBindHeaderRowViewHolder(viewHolder: ViewHolderImpl, row: Int) {
        val vh = viewHolder as HeaderRowViewHolder
        vh.headerRowTextView.text = rows[row]
    }

    override fun onBindLeftTopHeaderViewHolder(viewHolder: ViewHolderImpl) {
    }

    override fun getColumnWidth(column: Int): Int {
        return columnWidth
    }

    override fun getHeaderColumnHeight(): Int {
        return headerHeight
    }

    override fun getRowHeight(row: Int): Int {
        return rowHeight
    }

    override fun getHeaderRowWidth(): Int {
        return headerWidth
    }

    class ItemViewHolder(itemView: View) : ViewHolderImpl(itemView) {
        var itemTextView: TextView = itemView.findViewById(R.id.item_text)
    }

    class HeaderColumnViewHolder(itemView: View) : ViewHolderImpl(itemView) {
        var headerColumnTextView: TextView = itemView.findViewById(R.id.header_column_text)
    }

    class HeaderRowViewHolder(itemView: View) : ViewHolderImpl(itemView) {
        var headerRowTextView: TextView = itemView.findViewById(R.id.header_row_text)
    }

    class HeaderLeftTopViewHolder(itemView: View) : ViewHolderImpl(itemView)
}*/
