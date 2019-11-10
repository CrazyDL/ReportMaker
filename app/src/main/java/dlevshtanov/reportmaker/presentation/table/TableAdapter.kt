package dlevshtanov.reportmaker.presentation.table

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.TableEntity


class TableAdapter(context: Context) : AbstractTableAdapter<String, String, Int>(context) {

    fun initTable(columnTitles: List<String>, rowTitles: List<String>, cells: List<List<Int>>) {
        setAllItems(columnTitles, rowTitles, cells)
    }

    fun updateCell(item: TableEntity) {
        val rowIndex = mRowHeaderItems?.indexOf(item.rowTitle) ?: UNDEFENDED_VALUE
        val columnIndex = mColumnHeaderItems?.indexOf(item.columnTitle) ?: UNDEFENDED_VALUE
        if (rowIndex != UNDEFENDED_VALUE && columnIndex != UNDEFENDED_VALUE) {
            changeCellItem(columnIndex, rowIndex, item.value)
        }
    }

    fun clearCells() {
        setCellItems(MutableList(mRowHeaderItems.size) { MutableList(mColumnHeaderItems.size) { 0 } })
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return CellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Any,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell = cellItemModel as Int
        val viewHolder = holder as CellViewHolder
        viewHolder.cellTextView.text = cell.toString()
    }


    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_header_column, parent, false)
        return ColumnHeaderViewHolder(layout)
    }

    override fun onBindColumnHeaderViewHolder(holder: AbstractViewHolder, columnHeaderItemModel: Any, position: Int) {
        val columnHeader = columnHeaderItemModel as String

        val columnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.columnTextView.text = columnHeader

        columnHeaderViewHolder.columnTextView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        columnHeaderViewHolder.columnTextView.requestLayout()
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_header_row, parent, false)
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder, rowHeaderItemModel: Any, position: Int) {
        val rowHeader = rowHeaderItemModel as String

        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.rowTextView.text = rowHeader

        rowHeaderViewHolder.rowTextView.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        rowHeaderViewHolder.rowTextView.requestLayout()
    }

    override fun onCreateCornerView(): View {
        return LayoutInflater.from(mContext).inflate(R.layout.item_header_left_top, null, false)
    }

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(rowPosition: Int): Int {
        return 0
    }

    override fun getCellItemViewType(columnPosition: Int): Int {
        return 0
    }

    class CellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cellTextView: TextView = itemView.findViewById(R.id.item_text)
    }

    class ColumnHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val columnTextView: TextView = itemView.findViewById(R.id.header_column_text)
    }

    class RowHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val rowTextView: TextView = itemView.findViewById(R.id.header_row_text)
    }

    companion object {
        private const val UNDEFENDED_VALUE = -1
    }
}
