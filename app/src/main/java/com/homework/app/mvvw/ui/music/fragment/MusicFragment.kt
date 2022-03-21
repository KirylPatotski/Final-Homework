package com.omisoft.app.mvvw.ui.music.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omisoft.app.R
import com.omisoft.app.mvvw.data.network.service.last_fm.LastFMNetwork
import com.omisoft.app.mvvw.data.network.service.last_fm.LastFMNetworkImpl
import com.omisoft.app.mvvw.ui.music.favorite.GetStringImpl
import com.omisoft.app.mvvw.ui.music.favorite.draft.Favorite_activity


class MusicFragment : Fragment() {

    private val viewModel by viewModels<MusicViewModel>()
    private lateinit var recyclerArtists: RecyclerView
    private lateinit var openButton: AppCompatButton
    private lateinit var textfield: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_music, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastFMNetwork = LastFMNetworkImpl.getInstance() as LastFMNetwork
        viewModel.setArtistService(lastFMNetwork.getArtistsService())
        lifecycle.addObserver(viewModel)

        openButton = view.findViewById(R.id.open_favorite_button)
        openButton.text = "Exit"
        recyclerArtists = view.findViewById(R.id.recycler_artists)
        recyclerArtists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //unfortunately INT.MAX_VALUE as edgeOffset does not work
        print(Int.MAX_VALUE.toString())
        recyclerArtists.addItemDecoration(ArtistsRecyclerItemDecoration(64))
        textfield = view.findViewById(R.id.favoriteTextView)



        textfield.setOnClickListener{
            var a = GetStringImpl()
            a.getStr()
            var Text = a.toString()
            textfield.text = Text
        }


        openButton.setOnClickListener {
            val intent = Intent (activity, Favorite_activity::class.java)
            activity?.startActivity(intent)
        }


        subscribeToLiveData()



    }



    private fun subscribeToLiveData() {
        viewModel.artistsLiveData.observe(viewLifecycleOwner, { artists ->
            recyclerArtists.adapter = ArtistsAdapter(artists) { artist ->

            }
        })
    }

}