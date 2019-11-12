package dlevshtanov.reportmaker.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_layout)
        initViews()
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }

    override fun onItemClicked(item: HistoryEntity) {

    }

    private fun initViews() {
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

    companion object {

        fun getIntent(context: Context) = Intent(context, HistoryActivity::class.java)
    }
}