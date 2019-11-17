package dlevshtanov.reportmaker.presentation.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dlevshtanov.reportmaker.R
import java.text.SimpleDateFormat
import java.util.*

class ActionsWithHistoryBottomSheet : BottomSheetDialogFragment() {

    private lateinit var historyOptionsTextView: TextView
    private lateinit var resetCellTextView: TextView
    private lateinit var resetAfterDate: TextView
    private var historyCallback: HistoryCallback? = null
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HistoryCallback) {
            historyCallback = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_actions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = Date(arguments?.getLong(DATE_KEY) ?: 0)
        historyOptionsTextView = view.findViewById(R.id.history_actions_title_text_view)
        historyOptionsTextView.text = view.resources.getString(R.string.history_options_title, dateFormat.format(date))

        resetCellTextView = view.findViewById(R.id.reset_cell_value_text_view)
        resetCellTextView.setOnClickListener {
            historyCallback?.onResetCellClicked()
            this.dismiss()
        }

        resetAfterDate = view.findViewById(R.id.reset_after_date_text_view)
        resetAfterDate.setOnClickListener {
            historyCallback?.onResetAfterDateClicked()
            this.dismiss()
        }
    }

    companion object {

        const val TAG = "ActionsWithHistoryBottomSheet"
        private const val DATE_KEY = "DATE_KEY"

        fun newInstance(date: Long): ActionsWithHistoryBottomSheet {
            val bundle = Bundle().apply {
                putLong(DATE_KEY, date)
            }
            val dialog = ActionsWithHistoryBottomSheet()
            dialog.arguments = bundle
            return dialog
        }
    }
}