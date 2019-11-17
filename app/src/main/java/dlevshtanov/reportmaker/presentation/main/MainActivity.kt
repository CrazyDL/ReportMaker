package dlevshtanov.reportmaker.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import dlevshtanov.reportmaker.R
import dlevshtanov.reportmaker.models.Pages
import dlevshtanov.reportmaker.models.TitleEntity
import dlevshtanov.reportmaker.presentation.FragmentsViewModel
import dlevshtanov.reportmaker.presentation.history.HistoryActivity
import dlevshtanov.reportmaker.presentation.titles.TitlesCallback


class MainActivity : AppCompatActivity(), TitlesCallback {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainAdapter: MainPagerAdapter
    private lateinit var addTitleFab: FloatingActionButton
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var decreaseButton: ImageButton
    private lateinit var increaseButton: ImageButton
    private lateinit var cellValueTextView: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initViews()
        initListeners()
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.history_option -> {
                mainViewModel.onShowHistoryClicked()
                openHistory()
                true
            }
            R.id.clear_table_data_option -> {
                showClearTableDataDialog()
                true
            }
            R.id.clear_all_data_option -> {
                showClearAllDataDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FOR_HISTORY_ACTIVITY) {
            mainViewModel.updateTable()
        }
    }

    override fun onItemClicked(item: TitleEntity) {
        mainViewModel.onItemClicked(item)
    }

    override fun onItemDeleted(item: TitleEntity) {
        mainViewModel.deleteItem(item)
    }

    override fun onItemsChanged(items: List<TitleEntity>) {
        mainViewModel.updateItems(items)
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mainAdapter = MainPagerAdapter(this)
        addTitleFab = findViewById(R.id.fab)

        viewPager = findViewById(R.id.main_pager)
        viewPager.offscreenPageLimit = 2
        viewPager.isUserInputEnabled = false
        tabLayout = findViewById(R.id.tab_layout)
        viewPager.adapter = mainAdapter

        decreaseButton = findViewById(R.id.decrease_button)
        increaseButton = findViewById(R.id.increase_button)
        cellValueTextView = findViewById(R.id.cell_value_text_view)
    }

    private fun initListeners() {
        TabLayoutMediator(tabLayout, viewPager, object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = mainAdapter.getPageTitle(position)
            }
        }).attach()
        addTitleFab.setOnClickListener { mainViewModel.onFabClicked(this) }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mainViewModel.onPageSelected(Pages.getPageByPosition(position))
            }
        })
        toolbar.setNavigationOnClickListener { mainViewModel.resetSelectedItem() }
        decreaseButton.setOnClickListener { mainViewModel.onDecreaseClick() }
        increaseButton.setOnClickListener { mainViewModel.onIncreaseClick() }
    }

    private fun init() {
        val titlesViewModel = ViewModelProviders.of(this).get(FragmentsViewModel::class.java)
        mainViewModel =
            ViewModelProviders.of(this, MainViewModelFactory(titlesViewModel)).get(MainViewModel::class.java)
        mainViewModel.fabVisibilityLiveData.observe(this, Observer<Boolean> { isVisible ->
            if (isVisible) addTitleFab.show() else addTitleFab.hide()
        })
        mainViewModel.toolbarTitleLiveData.observe(this, Observer { title ->
            val isTitleEmpty = title.isNullOrEmpty()
            supportActionBar?.title = if (isTitleEmpty) mainAdapter.getPageTitle(viewPager.currentItem) else title
            toolbar.navigationIcon = if (isTitleEmpty) null else getDrawable(R.drawable.ic_close_white_24dp)

        })
        mainViewModel.showAddTitleDialogLiveData.observe(this, Observer<String> { title ->
            showAddTitleDialog(title)
        })
        mainViewModel.cellValueLiveData.observe(this, Observer<Int> { value ->
            cellValueTextView.text = value.toString()
        })
        mainViewModel.currentPageLiveData.observe(this, Observer<Pages> { page ->
            viewPager.currentItem = page.position
        })
        mainViewModel.alreadyExistsAlertLiveData.observe(this, Observer<Pages> { page ->
            showAlreadyExistDialog(if (page == Pages.ROW) R.string.row_exist_dialog_title else R.string.column_exist_dialog_title)
        })
        mainViewModel.initViewModel(Pages.getPageByPosition(viewPager.currentItem))
    }

    private fun showAddTitleDialog(title: String) {
        val dialogView = layoutInflater.inflate(R.layout.add_title_dialog, null)
        val titleEditText: TextInputEditText = dialogView.findViewById(R.id.title_edit_text)
        titleEditText.requestFocus()
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(title)
            .setPositiveButton(getString(R.string.dialog_add)) { _, _ ->
                mainViewModel.onTitleAdded(titleEditText.text.toString())
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .create()
        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        alertDialog.show()
    }

    private fun showClearTableDataDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.clear_table_data_dialog_title)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                mainViewModel.onClearTableDataClicked()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun showClearAllDataDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.clear_all_data_dialog_title)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                mainViewModel.onClearAllDataClicked()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun showAlreadyExistDialog(title: Int) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ -> }
            .create()
            .show()
    }

    private fun openHistory() {
        startActivityForResult(HistoryActivity.getIntent(this), REQUEST_CODE_FOR_HISTORY_ACTIVITY)
    }

    companion object {
        const val REQUEST_CODE_FOR_HISTORY_ACTIVITY = 101
    }
}
