package com.priyam.squareboatapplication.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyam.squareboatapplication.R
import com.priyam.squareboatapplication.`interface`.IconsSetListRecyclerClickListener
import com.priyam.squareboatapplication.adapter.IconSetAdapter
import com.priyam.squareboatapplication.model.Iconset
import com.priyam.squareboatapplication.util.Resource
import com.priyam.squareboatapplication.util.blockUI
import com.priyam.squareboatapplication.util.showToast
import com.priyam.squareboatapplication.util.unBlockUI
import com.priyam.squareboatapplication.viewModel.MainActivityViewModel
import com.priyam.squareboatapplication.viewModel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import java.io.Serializable
import java.util.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var mainActivityViewModel: MainActivityViewModel

    var count=20
    lateinit var progressbar: ProgressBar
    lateinit var noIconText: TextView
    lateinit var recyclerView: RecyclerView

    val limitValue=20
    private val iconsSetList= ArrayList<Iconset>()
    private lateinit var iconSetAdapter: IconSetAdapter
    var isLoading = false
    var isLastRecord = false
    var isScrolling = false
     var isPremium: Boolean= true

    private var filter: ImageView?=null

    private val recyclerViewListener=object :IconsSetListRecyclerClickListener {
        override fun onDetailButtonClick(position: Int) {
            val intent = Intent(this@MainActivity, IconListActivity::class.java)
            intent.putExtra("iconSet", iconsSetList[position].iconset_id as Serializable)
            startActivity(intent)
        }
    }

    private val scrollListener= object: RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager= recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastRecord
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= limitValue
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                if(count<150){
                    iconsSetList.clear()
                    mainActivityViewModel.getIconSet(count.toString(),isPremium)
                    isScrolling = false}
            } else {

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressbar = findViewById(R.id.progressBar)
        noIconText = findViewById(R.id.noIconText)
        recyclerView = findViewById(R.id.recyclerView)
        filter = findViewById(R.id.filter)
        mainActivityViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainActivityViewModel::class.java)
        setUpOnClickListeners()
        setupRecyclerView()
        mainActivityViewModel.getIconSet(count.toString(),isPremium)
        initObservables()
    }

    private fun initObservables(){
        mainActivityViewModel.iconSetResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    progressbar.visibility = View.GONE
                    this?.unBlockUI()
                    isLoading = false
                    val totalIcons = response.data?.total_count

                    val mIconsList = response.data?.iconsets
                    if (!mIconsList.isNullOrEmpty()) {
                        iconsSetList.addAll(mIconsList)
                        iconSetAdapter.notifyDataSetChanged()
                        count += 20
                    } else {
                        iconsSetList.clear()
                        iconSetAdapter.notifyDataSetChanged()
                        progressbar.visibility = View.GONE
                        noIconText.visibility = View.VISIBLE
                        Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show()
                    }

                    if (totalIcons != null)
                        isLastRecord = totalIcons == count.toString()


                }

                is Resource.Error -> {
                    progressbar.visibility = View.GONE
                    isLoading = false
                    noIconText.visibility = View.VISIBLE
                    this?.unBlockUI()
                    response.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    isLoading = true
                    progressbar.visibility = View.VISIBLE
                    this?.blockUI()
                }
            }

        })
    }

    private fun setupRecyclerView() {
        iconSetAdapter= IconSetAdapter(iconsSetList, recyclerViewListener)
        recyclerView.apply {
            adapter = iconSetAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }

    private fun setUpOnClickListeners() {
        filter?.setOnClickListener{
            openFilterDialog()
        }
    }

    private fun openFilterDialog() {

        val checkedItem = intArrayOf(-1)


            val alertDialog = AlertDialog.Builder(this@MainActivity)


            alertDialog.setTitle("Choose Type")

            val listItems = arrayOf("Paid", "Free")


            alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->

                checkedItem[0] = which


                var premium= listItems[which]
               isPremium= premium != "Free"
                showToast(premium)
                count =20
                iconsSetList.clear()

                iconSetAdapter.notifyDataSetChanged()
                mainActivityViewModel.getIconSet(count.toString(),isPremium)

                dialog.dismiss()
            }


            alertDialog.setNegativeButton("Cancel") { dialog, which -> }


            val customAlertDialog = alertDialog.create()


            customAlertDialog.show()


    }

}