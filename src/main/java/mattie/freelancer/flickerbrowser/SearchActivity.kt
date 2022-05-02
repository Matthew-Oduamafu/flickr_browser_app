package mattie.freelancer.flickerbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.navigation.ui.AppBarConfiguration
import androidx.preference.PreferenceManager
import mattie.freelancer.flickerbrowser.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "SearchActivity"
    private var searchView: SearchView? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
        activateToolbar(true)
        Log.d(TAG, "onCreate: starts")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
        Log.d(TAG, "onCreateOptionsMenu: $componentName")
        Log.d(TAG, "onCreateOptionsMenu: hints is ${searchView?.queryHint}")
        Log.d(TAG, "onCreateOptionsMenu: $searchableInfo")

        searchView?.isIconified = false

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: called")

                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPreferences.edit().putString(FLICKR_QUERY, query?.trim()).apply()
                searchView?.clearFocus()

                finish()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        Log.d(TAG, "onCreateOptionsMenu: returning")
        return true
    }
}