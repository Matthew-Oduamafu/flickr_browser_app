package mattie.freelancer.flickerbrowser

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

internal const val FLICKR_QUERY = "FLICKR_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"

open class BaseActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "BaseActivity"

    // declaring a function the to add home(back <-) button to the other activities
    // except the home button
    internal fun activateToolbar(enableHome: Boolean) {
        Log.d(TAG, "activateToolbar: called")

        // val toolbar: View = findViewById(R.id.toolbar) as Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar) // older way of doing it is above
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}