package mattie.freelancer.flickerbrowser

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*
import mattie.freelancer.flickerbrowser.databinding.ActivityPhotoDetailsBinding

class PhotoDetailsActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
        activateToolbar(true)

        // getting the data from the intent
//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        // we use the code above when we use serializable

        val photo = intent.getParcelableExtra<Photo>(PHOTO_TRANSFER)

        // now we set the extracted data
//        photo_title.text = photo?.title
//        photo_tags.text = photo?.tags
//        photo_author.text = photo?.author

        photo_title.text = resources.getString(R.string.photo_title_text, photo?.title)
        photo_tags.text = resources.getString(R.string.photo_tags_text, photo?.tags)
        photo_author.text = resources.getString(R.string.photo_author_text, photo?.author)

        Picasso.get().load(photo?.link).error(R.drawable.error_image)
            .placeholder(R.drawable.placeholder).into(photo_image)
    }
}