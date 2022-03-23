package com.homework.app.mvvw.ui.music.fragment

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
import com.homework.app.R
import com.homework.app.mvp.LastFMNetwork
import com.homework.app.mvp.LastFMNetworkImpl
import com.homework.app.mvvw.ui.browser.BrowserFragment
import com.homework.app.mvvw.ui.main.MainActivity


class MusicFragment : Fragment() {

    private val viewModel by viewModels<MusicViewModel>()
    private lateinit var recyclerArtists: RecyclerView
    private lateinit var openButton: AppCompatButton
    private lateinit var textfield: TextView
    private lateinit var favorite: FavoriteInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_music, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastFMNetwork = LastFMNetworkImpl.getInstance() as LastFMNetwork
        viewModel.setArtistService(lastFMNetwork.getArtistsService())
        lifecycle.addObserver(viewModel)

        openButton = view.findViewById(R.id.open_browser_button)
        recyclerArtists = view.findViewById(R.id.recycler_artists)
        recyclerArtists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //unfortunately INT.MAX_VALUE as edgeOffset does not work there are only 50 artist or so
        recyclerArtists.addItemDecoration(ArtistsRecyclerClass(100))
        textfield = view.findViewById(R.id.favoriteTextView)


        setListeners()
        subscribeToLiveData()
    }

    private fun setListeners(){

        textfield.setOnClickListener{
            favorite = FavoriteInfo()
            var adapterCompanionObject = Adapter.ViewHolder.favorite
            var textFieldText = adapterCompanionObject.toString()
            textfield.text = textFieldText

        }
        openButton.setOnClickListener {
            (activity as MainActivity).openFragment(BrowserFragment(), doClearBackStack = false)
        }

    }

    private fun subscribeToLiveData() {
        viewModel.artistsLiveData.observe(viewLifecycleOwner, { artists ->
            recyclerArtists.adapter = Adapter(artists) { artist ->

            }
        })
    }

}