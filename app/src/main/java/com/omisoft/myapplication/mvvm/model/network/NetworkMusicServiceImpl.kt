package com.omisoft.myapplication.mvvm.model.network

import com.omisoft.myapplication.mvvm.model.entity.Album

class NetworkMusicServiceImpl : NetworkMusicService {

    private val albums = listOf(
        Album("Dynamite", "https://lastfm.freetls.fastly.net/i/u/174s/41b15d8a0ad6a81323b598bfb19cede9.png"),
        Album("Dynamite (Extended)", "https://lastfm.freetls.fastly.net/i/u/174s/0e41a35afb8e2ad81aca9621d420a33f.png"),
        Album("Funk Wav Bounces Vol.1", "https://lastfm.freetls.fastly.net/i/u/174s/a552de6f9f15614d4da59ebf8cd7f5e2.png"),
        Album("pHysicAl", "https://lastfm.freetls.fastly.net/i/u/174s/2894a2fe8c099b5822bd89e0615476f9.png"),
        Album("Abba Gold Anniversary Edition", "https://lastfm.freetls.fastly.net/i/u/174s/21f77cd622ec3ab6e29f13b4a48c9e9d.png"),
        Album("Take the Heat Off Me", "https://lastfm.freetls.fastly.net/i/u/174s/4ca30a06b6bd13a8a694d7c32349151e.png"),
        Album(
            "Glee: The Music, The Complete Season Two",
            "https://lastfm.freetls.fastly.net/i/u/174s/08c1c917d3b841c990955d5530b1301a.png"
        ),
        Album("She Wolf (Expanded Edition)", "https://lastfm.freetls.fastly.net/i/u/174s/4e856b1f6d0c2a476643b90dbe37a4ea.png"),
    )

    override fun getFavouriteMusic(): List<Any> {
        TODO("Not yet implemented")
    }

    override fun getAlbums(): List<Album> = albums

    override fun updateFavouriteMusic(data: Any) {
        TODO("Not yet implemented")
    }
}