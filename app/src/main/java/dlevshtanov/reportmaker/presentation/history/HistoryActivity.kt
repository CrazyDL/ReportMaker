package dlevshtanov.reportmaker.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.HistoryEntity

class HistoryActivity : AppCompatActivity(), HistoryCallback {


    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)
        initViews()
        init()
    }

    override fun onItemClicked(item: HistoryEntity) {
        historyViewModel.currentItem = item
        ActionsWithHistoryBottomSheet.newInstance(item.date)
            .show(supportFragmentManager, ActionsWithHistoryBottomSheet.TAG)
    }

    override fun onResetCellClicked() {
        showResetCellDialog()
    }

    override fun onResetAfterDateClicked() {
        showResetAfterDateDialog()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        historyAdapter = HistoryAdapter(this)
        recyclerView = findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = historyAdapter
    }

    private fun init() {
        historyViewModel = ViewModelProviders.of(this, HistoryViewModelFactory()).get(HistoryViewModel::class.java)
        historyViewModel.initHistoryLiveData.observe(this, Observer {
            historyAdapter.setItems(it)
            recyclerView.scrollToPosition(it.size - 1)
        })
        historyViewModel.initItems()
    }

    private fun showResetCellDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_cell_dialog_title)
            .setMessage(R.string.reset_cell_dialog_description)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                historyViewModel.onResetCellClicked()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun showResetAfterDateDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_after_date_dialog_title)
            .setMessage(R.string.reset_after_date_dialog_description)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                historyViewModel.onResetAfterDateClicked()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .create()
            .show()
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, HistoryActivity::class.java)
    }
}