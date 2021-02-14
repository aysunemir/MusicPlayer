package com.aemir.demo.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aemir.demo.musicplayer.data.entities.Song
import com.aemir.demo.musicplayer.databinding.ListItemBinding
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    inner class SongViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindSong(song: Song) {
            binding.apply {
                tvPrimary.text = song.title
                tvSecondary.text = song.subtitle
                glide.load(song.imageUrl).into(ivItemImage)

                root.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(song)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bindSong(song)
    }

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.mediaId == newItem.mediaId

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}