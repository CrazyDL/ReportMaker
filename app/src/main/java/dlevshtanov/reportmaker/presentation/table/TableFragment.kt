package dlevshtanov.reportmaker.presentation.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.evrencoskun.tableview.TableView
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.presentation.FragmentsViewModel

class TableFragment : Fragment() {

    private lateinit var tableAdapter: TableAdapter
    private lateinit var fragmentsViewModel: FragmentsViewModel
    private lateinit var tableView: TableView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableAdapter = TableAdapter(view.context)
        tableView = view.findViewById(R.id.table_view)
        tableView.isShowHorizontalSeparators = true
        tableView.isShowVerticalSeparators = true
        tableView.adapter = tableAdapter
        initObservers()
    }

    private fun initObservers() {
        activity?.let { fragmentsViewModel = ViewModelProviders.of(it).get(FragmentsViewModel::class.java) }
        fragmentsViewModel.initTableLiveData.observe(this, Observer { items ->
            tableAdapter.initTable(items.first, items.second, items.third)
        })
        fragmentsViewModel.updateCellLiveData.observe(this, Observer { item ->
            tableAdapter.updateCell(item)
        })
        fragmentsViewModel.clearCellsLiveData.observe(this, Observer {
            tableAdapter.clearCells()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = TableFragment()
    }
}