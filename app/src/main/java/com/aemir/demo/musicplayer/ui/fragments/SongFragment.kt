package com.aemir.demo.musicplayer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aemir.demo.musicplayer.R
import com.aemir.demo.musicplayer.data.entities.Song
import com.aemir.demo.musicplayer.databinding.FragmentSongBinding
import com.aemir.demo.musicplayer.exoplayer.toSong
import com.aemir.demo.musicplayer.other.Status
import com.aemir.demo.musicplayer.ui.viewmodels.MainViewModel
import com.aemir.demo.musicplayer.ui.viewmodels.SongViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    private lateinit var binding: FragmentSongBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val songViewModel: SongViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    private var curPlayingSong: Song? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSongBinding.bind(view)
        subscribeToObservers()
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} * ${song.subtitle}"
        binding.tvSongName.text = title
        glide.load(song.imageUrl).into(binding.ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                if (result.status == Status.SUCCESS) {
                    result.data?.let { songs ->
                        if (curPlayingSong == null && songs.isNotEmpty()) {
                            curPlayingSong = songs[0]
                            updateTitleAndSongImage(songs[0])
                        }
                    }
                }
            }
        }

        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }
    }

}