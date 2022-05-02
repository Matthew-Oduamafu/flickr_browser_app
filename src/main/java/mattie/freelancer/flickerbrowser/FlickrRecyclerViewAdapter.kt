package mattie.freelancer.flickerbrowser

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickrImageViewHolder>() {
    @Suppress("PrivatePropertyName")
    private val TAG = "FlickrRecyclerViewAdapt"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        // Called byt the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)

        return FlickrImageViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {

        if (photoList.isEmpty()) {
            Log.d(TAG, "onBindViewHolder: yes photoList is empty")
            holder.thumbnail.setImageResource(R.drawable.error_image)
            holder.title.setText(R.string.no_photo_found_text)
//            holder.title.text = view.resources.getString(R.string.no_photo_found_text)
        } else {
            // called to add data to a view holder
            val photoItem = photoList[position]
//        Log.d(TAG, "onBindViewHolder: ${photoItem.title} --> position")

            Picasso.get().load(photoItem.image).error(R.drawable.error_image)
                .placeholder(R.drawable.placeholder).into(holder.thumbnail)
            // setting the title
            holder.title.text = photoItem.title
        }
    }

    override fun getItemCount(): Int {
//        Log.d(TAG, "getItemCount: called")
        return if (photoList.isNotEmpty()) photoList.size else 1
    }
}