package dlevshtanov.reportmaker.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.HistoryEntity

class HistoryAdapter(private val historyCallback: HistoryCallback) : RecyclerView.Adapter<HistoryViewHolder>() {

    private val items = mutableListOf<HistoryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { historyCallback.onItemClicked(item) }
    }

    fun setItems(newItems: List<HistoryEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}