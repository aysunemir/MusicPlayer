package com.aemir.demo.musicplayer.adapters

import android.view.View
import com.aemir.demo.musicplayer.R
import com.aemir.demo.musicplayer.data.entities.Song
import com.aemir.demo.musicplayer.databinding.SwipeItemBinding
import javax.inject.Inject

class SwipeSongAdapter @Inject constructor() : BaseSongAdapter(R.layout.swipe_item) {

    override fun bindSong(view: View, song: Song) {
        SwipeItemBinding.bind(view).apply {
            val text = "${song.title} - ${song.subtitle}"
            tvPrimary.text = text

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

}