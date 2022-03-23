package com.homework.app.mvvw.ui.music.fragment

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homework.app.R
import com.homework.app.mvp.Artist
import com.homework.app.mvvw.ui.browser.BrowserFragment


class Adapter(private val artists: List<Artist>, private val artistSelected: (Artist) -> Unit) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.artist_recycler, parent, false),artistSelected)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setArtist(artists[position])
    }

    override fun getItemCount(): Int = artists.size

//View holder
@SuppressLint("ResourceType")
class ViewHolder(private val view: View, private val artistSelected: (Artist) -> Unit) : RecyclerView.ViewHolder(view) {

    companion object {
        var favorite = "You have no favorites"
    }

    private var appcompatimgaeview: AppCompatImageView = view.findViewById(R.id.image_artist)
    private var title: AppCompatTextView = view.findViewById(R.id.artist_name)
    private var artist: Artist? = null
    private var likeButton: AppCompatButton = view.findViewById(R.id.heart_button)
    private var count: AppCompatTextView = view.findViewById(R.id.artist_count)
    private var urlButton: AppCompatButton = view.findViewById(R.id.url_adapter_button)


    init {
        view.setOnClickListener {
            artist?.let {
                artistSelected(it)
            }
        }

        likeButton.setOnClickListener {
            println(adapterPosition)
            favorite = title.text.toString()
            likeButton.text = "💛"
        }

        urlButton.setOnClickListener{
            println(adapterPosition)
            favorite = title.text.toString()
            likeButton.text = "💛"
            val activity = view.context as AppCompatActivity
            val myFragment: Fragment = BrowserFragment()
               activity.supportFragmentManager.beginTransaction()
                   .setCustomAnimations(R.anim.alpha,R.anim.alpha_out)
                   .replace(R.id.main_fragment_container, myFragment).addToBackStack(null).commit()

        }
    }


    fun setArtist(artist: Artist) {
        Glide.with(view.context)
            .load(artist.image.last())
            .into(appcompatimgaeview)

        count.text = artist.playcount.toString()
        title.text = artist.name
    }


}
}

class FavoriteInfo(){
        var link = "https://www.last.fm/"
        var name = "You have no favorites"
}

//Recycler View
class ArtistsRecyclerClass(private val edgeOffset: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.right = 0
        outRect.left = 0

        when (position) {
            itemCount - 1 -> {
                outRect.right = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, edgeOffset.toFloat(), view.context.resources.displayMetrics
                ).toInt()
                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, edgeOffset.toFloat(), view.context.resources.displayMetrics
                ).toInt()
            }
            else -> {
                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, edgeOffset.toFloat(), view.context.resources.displayMetrics
                ).toInt()
            }
        }
    }
}

//        private val imageButton: AppCompatButton = view.findViewById(R.id.image)
//    <Button
//    android:id="@id/image"
//    android:layout_width="200dp"
//    android:layout_height="200dp"
//    android:background="@android:drawable/btn_dialog"
//    android:text="Something went wrong"
//    app:layout_constraintEnd_toEndOf="parent"
//    app:layout_constraintStart_toStartOf="parent"
//    app:layout_constraintTop_toTopOf="parent"
//    />






