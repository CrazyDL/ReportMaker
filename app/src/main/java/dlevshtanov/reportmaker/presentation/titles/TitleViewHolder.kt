package dlevshtanov.reportmaker.presentation.titles

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.R
import github.nisrulz.recyclerviewhelper.RVHViewHolder

class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RVHViewHolder {

    private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)


    fun bind(title: String) {
        titleTextView.text = title
    }

    override fun onItemSelected(actionstate: Int) = Unit

    override fun onItemClear() = Unit
}