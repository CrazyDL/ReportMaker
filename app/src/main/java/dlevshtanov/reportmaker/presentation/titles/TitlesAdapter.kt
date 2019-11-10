package dlevshtanov.reportmaker.presentation.titles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.TitleEntity
import github.nisrulz.recyclerviewhelper.RVHAdapter
import java.util.*

class TitlesAdapter(private val titlesCallback: TitlesCallback) : RecyclerView.Adapter<TitleViewHolder>(), RVHAdapter {

    private val items = mutableListOf<TitleEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.title_element, parent, false)
        return TitleViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = items[position]
        item.orderId = position
        holder.bind(item.title)
        holder.itemView.setOnClickListener { titlesCallback.onItemClicked(item) }
    }

    override fun onItemDismiss(position: Int, direction: Int) {
        val deletedItem = items[position]
        items.removeAt(position)
        notifyItemRemoved(position)
        titlesCallback.onItemDeleted(deletedItem)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        items.forEachIndexed { i, item -> item.orderId = i }
        titlesCallback.onItemsChanged(items)
        return false
    }

    fun addToEnd(item: TitleEntity) {
        item.orderId = items.size
        items.add(item)
        notifyItemInserted(items.size)
        titlesCallback.onItemsChanged(listOf(item))
    }

    fun setItems(newItems: List<TitleEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}