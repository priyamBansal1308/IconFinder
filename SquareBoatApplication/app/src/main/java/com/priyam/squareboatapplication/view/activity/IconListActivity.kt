package com.priyam.squareboatapplication.view.activity

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyam.squareboatapplication.R
import com.priyam.squareboatapplication.`interface`.IconsListRecyclerClickListener
import com.priyam.squareboatapplication.adapter.IconListAdapter
import com.priyam.squareboatapplication.model.Icon
import com.priyam.squareboatapplication.util.*
import com.priyam.squareboatapplication.viewModel.IconsListViewModel
import com.priyam.squareboatapplication.viewModel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class IconListActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var iconListViewModel: IconsListViewModel
    var count1=100
    lateinit var progressbar: ProgressBar
    lateinit var noIconText: TextView
    lateinit var recyclerView: RecyclerView
    var iconsOffSetValue=0

    private var iconsList= ArrayList<Icon>()
    private var filteredIconsList = ArrayList<Icon>()
    private lateinit var iconListAdapter: IconListAdapter
    lateinit var downloadManager: DownloadManager
    lateinit var imageUrl: String
    lateinit var imageName: String

    var isLoading = false
    var isLastRecord = false
    var isScrolling = false
    lateinit var iconSet_id: String
    var limitValue = 20
    var totalIcons: Int =0
    lateinit var searchView: androidx.appcompat.widget.SearchView

    private val iconsRecyclerViewListener=object : IconsListRecyclerClickListener {
        override fun onDetailButtonClick(position: Int) {
            var icon = iconsList[position]
            if(icon.is_premium==true)
                showToast("Image is paid and can be downloaded only after payment")
            else {
                //imageUrl = "https://www.freeiconspng.com/downloadimg/23494"
                 imageUrl =
                         icon.raster_Sizes?.get(icon.raster_Sizes!!.size - 1)!!.formats[0].download_url.toString()

                imageName = icon.icon_id.toString()
                println(imageUrl)

                isStoragePermissionGranted()
            }

        }
    }

    private val scrollListener= object: RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            super.onScrollStateChanged(recyclerView, newState)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_list)
         searchView=findViewById(R.id.vehicle_search_view) as androidx.appcompat.widget.SearchView
         iconSet_id = intent.getSerializableExtra("iconSet").toString()
        showToast(iconSet_id.toString())

        progressbar = findViewById(R.id.progressBar)
        noIconText = findViewById(R.id.noIconText)
        recyclerView = findViewById(R.id.iconListrecyclerView)
        iconListViewModel = ViewModelProvider(this, viewModelProviderFactory).get(IconsListViewModel::class.java)
        setUpOnClickListeners()
        setupRecyclerView()
        iconListViewModel.getIcon(count1, iconSet_id.toString())
        initObservables()


    }
    private fun initObservables(){
        iconListViewModel.iconResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    progressbar.visibility = View.GONE
                    this?.unBlockUI()
                      isLoading = false
                     totalIcons = response.data?.total_count!!.toInt()

                    val mIconsList = response.data?.icons
                    if (!mIconsList.isNullOrEmpty()) {
                        iconsList.addAll(mIconsList)
                        iconListAdapter.notifyDataSetChanged()
//                        if(count1<totalIcons!!.toInt())
                             count1 += 20

                    } else {
                        iconsList.clear()
                        iconListAdapter.notifyDataSetChanged()
                        progressbar.visibility = View.GONE
                        noIconText.visibility = View.VISIBLE
                        Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show()
                    }

                    if (totalIcons!=null)
                      isLastRecord= totalIcons.toInt() == count1

                    println(isLastRecord)
                    println(count1)
                    println(totalIcons)

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
        iconListAdapter= IconListAdapter(iconsList, iconsRecyclerViewListener)
        recyclerView.apply {
            adapter = iconListAdapter
            layoutManager = GridLayoutManager(this@IconListActivity, 2)
            addOnScrollListener(this@IconListActivity.scrollListener)
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                === PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("TAG", "Permission is granted")
                downloadIconManager()
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else {
            Log.v("TAG", "Permission is granted")
            downloadIconManager()
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0])
            downloadIconManager()
        }
    }

    private fun downloadIconManager() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(imageUrl)
        Log.v("downloadUrl1", imageUrl)
        Log.v("downloaduri1", uri.toString())
        val request = DownloadManager.Request(uri)
        request.addRequestHeader("Authorization",Constants.token)
        request.setTitle(imageName)
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
                "$imageName.png"
        )
        downloadManager.enqueue(request)
        Toast.makeText(this, "Downloading icon!", Toast.LENGTH_SHORT).show()
    }

    private fun setUpOnClickListeners(){

        val editText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        //editText.setTextColor(Color.WHITE)
        editText.setHintTextColor(Color.GRAY)
        editText.hint = "Search Icons By any Keyword"
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)



        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.length>2) {
                    var searchQuery = newText
                    showToast(searchQuery)
                    noIconText.visibility = View.GONE
                    count1 =20
                    iconsList.clear()
                    iconListAdapter.notifyDataSetChanged()
                    iconListViewModel.searchIcon(searchQuery)
                }
                else if(newText.isEmpty()){
                    count1=20
                    iconsList.clear()
                    iconListAdapter.notifyDataSetChanged()
                    noIconText.visibility = View.GONE

                    iconListViewModel.getIcon(count1, iconSet_id)
                }

                return false
            }
        })
    }
}