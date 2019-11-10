package dlevshtanov.reportmaker.presentation.titles

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dlevshtanov.reportmaker.presentation.FragmentsViewModel
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback

class ColumnFragment : Fragment() {

    private lateinit var fragmentsViewModel: FragmentsViewModel
    private lateinit var titlesCallback: TitlesCallback
    private lateinit var recyclerView: RecyclerView
    private lateinit var titlesAdapter: TitlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(dlevshtanov.reportmaker.R.layout.fragment_lists, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TitlesCallback) {
            titlesCallback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titlesAdapter = TitlesAdapter(titlesCallback)
        recyclerView = view.findViewById(dlevshtanov.reportmaker.R.id.titles_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = titlesAdapter

        val callback = RVHItemTouchHelperCallback(titlesAdapter, true, true, true)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recyclerView)
        initObservers()
    }

    private fun initObservers() {
        activity?.let { fragmentsViewModel = ViewModelProviders.of(it).get(FragmentsViewModel::class.java) }
        fragmentsViewModel.addColumnLiveData.observe(this, Observer { item ->
            titlesAdapter.addToEnd(item)
        })
        fragmentsViewModel.initColumnsLiveData.observe(this, Observer { items ->
            titlesAdapter.setItems(items)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ColumnFragment()
    }
}