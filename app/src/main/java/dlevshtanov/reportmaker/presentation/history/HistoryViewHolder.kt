package dlevshtanov.reportmaker.presentation.history

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.HistoryEntity
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val rowTextView: TextView = itemView.findViewById(R.id.row_text_view)
    private val columnTextView: TextView = itemView.findViewById(R.id.column_text_view)
    private val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
    private val changedTextView: TextView = itemView.findViewById(R.id.changed_text_view)
    private val wasTextView: TextView = itemView.findViewById(R.id.was_text_view)
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun bind(item: HistoryEntity) {
        val sign = if (item.changedValue > 0) "+" else ""
        val changedText = "$sign${item.changedValue}"
        rowTextView.text = item.rowTitle
        columnTextView.text = item.columnTitle
        dateTextView.text = dateFormat.format(Date(item.date))
        changedTextView.text = changedText
        wasTextView.text = item.wasValue.toString()
    }
}