package mattie.freelancer.flickerbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_first.*
import mattie.freelancer.flickerbrowser.databinding.ActivityMainBinding

const val URL_LINK =
    "https://www.flickr.com/services/feeds/photos_public.gne?tags=android,oreo,sdk&tagmode=any&format=json&nojsoncallback=1"

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecycleClickListener {

    @Suppress("PrivatePropertyName")
    private val TAG = "MainActivity"
    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private fun createUri(
        baseUrl: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, "createUri: called to create url")

        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang).appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
//        activateToolbar(false)

        ///*
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        //*/
        /**All the codes above were generated**/
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickrRecyclerViewAdapter

        /*
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        */
        Log.d(TAG, "onCreate: ends")
    }

    override fun onResume() {
        Log.d(TAG, "onResume: starts")
        super.onResume()

        /**Checking for internet connectivity**/
        if(!InternetConnection.isInternetAvailable(this)){
            Toast.makeText(this, "No internet available", Toast.LENGTH_LONG).show()
            Toast.makeText(this, "Turn on Mobile data or Wifi", Toast.LENGTH_LONG).show()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPreferences.getString(FLICKR_QUERY, "")

        if (queryResult!!.isNotEmpty()) {
            Log.d(TAG, "onResume: called with query $queryResult")
            // calling the asyncTask to perform action in background
            val url = createUri(
                "https://www.flickr.com/services/feeds/photos_public.gne",
                queryResult,
                "en-us",
                true
            )
            Log.d(TAG, "onResume: called url is $url")
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        } else if (queryResult.isEmpty()) {
            Log.d(TAG, "onResume: called query is empty")
            val url = createUri(
                "https://www.flickr.com/services/feeds/photos_public.gne",
                "china",
                "en-us",
                true
            )
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
        Log.d(TAG, "onResume: ends")
    }

    /**Implementing the recycler click listener**/


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu: called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick: starts")
//        Toast.makeText(this, "Long tap at position $position", Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected: called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp: called")
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "downloadComplete: called")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "downloadComplete: failed with status $status error message $data")
        }
    }


    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable: called")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable: ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, "onError: called error is $exception")
    }
}