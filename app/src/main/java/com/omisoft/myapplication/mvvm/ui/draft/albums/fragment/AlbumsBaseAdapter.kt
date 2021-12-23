package com.omisoft.myapplication.mvvm.ui.draft.albums.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.data.storage.room.entity.Album

class AlbumsBaseAdapter(private val albums: List<Album>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int = albums.size

    override fun getItem(position: Int): Album {
        return albums[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, oldView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.layout_album_item, parent, false)
        if (oldView != null) {
            view = oldView
        }

        val album = albums[position]
        val image = view.findViewById<ImageView>(R.id.album_logo)

        view.findViewById<TextView>(R.id.album_name).text = album.name
        Glide.with(context).load(album.imageUrl).into(image)

        return view
    }
}