package com.aemir.demo.musicplayer.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aemir.demo.musicplayer.R
import com.aemir.demo.musicplayer.data.entities.Song
import com.aemir.demo.musicplayer.databinding.FragmentSongBinding
import com.aemir.demo.musicplayer.exoplayer.isPlaying
import com.aemir.demo.musicplayer.exoplayer.toSong
import com.aemir.demo.musicplayer.other.Status
import com.aemir.demo.musicplayer.ui.viewmodels.MainViewModel
import com.aemir.demo.musicplayer.ui.viewmodels.SongViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    private var _binding: FragmentSongBinding? = null
    private val binding: FragmentSongBinding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val songViewModel: SongViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    private var curPlayingSong: Song? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekBar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSongBinding.bind(view)
        subscribeToObservers()

        binding.apply {

            ivPlayPauseDetail.setOnClickListener {
                curPlayingSong?.let {
                    mainViewModel.playOrToggleSong(it, true)
                }
            }

            ivSkip.setOnClickListener {
                mainViewModel.skipToNextSong()
            }

            ivSkipPrevious.setOnClickListener {
                mainViewModel.skipToPreviousSong()
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        setCurPlayerTimeToTextView(progress.toLong())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    shouldUpdateSeekBar = false
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    binding.seekBar.let {
                        mainViewModel.seekTo(it.progress.toLong())
                        shouldUpdateSeekBar = true
                    }
                }
            })
        }

    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
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

        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.apply {

                ivPlayPauseDetail.setImageResource(
                    if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
                )

                seekBar.progress = it?.position?.toInt() ?: 0

            }
        }

        songViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekBar) {
                binding.seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }

        songViewModel.curSongDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.tvSongDuration.text = dateFormat.format(it)
        }

    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.tvCurTime.text = dateFormat.format(ms)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}