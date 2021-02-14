package com.aemir.demo.musicplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aemir.demo.musicplayer.data.entities.Song

abstract class BaseSongAdapter(private val layoutId: Int) :
    ListAdapter<Song, BaseSongAdapter.SongViewHolder>(SongDiffCallback()) {

    inner class SongViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(song: Song) {
            bindSong(view, song)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layoutId, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.onBind(song)
    }

    protected var onItemClickListener: ((Song) -> Unit)? = null

    fun setItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    abstract fun bindSong(view: View, song: Song)
}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.mediaId == newItem.mediaId

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}