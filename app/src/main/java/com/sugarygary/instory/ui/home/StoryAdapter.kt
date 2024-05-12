package com.sugarygary.instory.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sugarygary.instory.R
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.databinding.ItemStoryBinding
import com.sugarygary.instory.util.glide
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class StoryAdapter(private val onClickRoot: (String) -> Unit) :
    ListAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.parent = parent
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvItemName.text = item.name
            tvItemName2.text = item.name
            tvDescription.text = item.description
            ivItemPhoto.glide(item.photoUrl)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateTime = LocalDateTime.parse(item.createdAt, formatter)
            val currentTime = LocalDateTime.now(ZoneOffset.UTC)
            val difference = ChronoUnit.SECONDS.between(dateTime, currentTime)

            val seconds = difference % 60
            val minutes = (difference / 60) % 60
            val hours = (difference / (60 * 60)) % 24
            val days = (difference / (60 * 60 * 24)) % 7
            val weeks = difference / (60 * 60 * 24 * 7)

            when {
                weeks > 0 -> tvTimestamps.text =
                    parent.context.getString(R.string.story_week_timestamp, weeks)

                days > 0 -> tvTimestamps.text =
                    parent.context.getString(R.string.story_day_timestamp, days)

                hours > 0 -> tvTimestamps.text =
                    parent.context.getString(R.string.story_hour_timestamp, hours)

                minutes > 0 -> tvTimestamps.text =
                    parent.context.getString(R.string.story_minute_timestamp, minutes)

                else -> tvTimestamps.text =
                    parent.context.getString(R.string.story_second_timestamp, seconds)
            }
            root.setOnClickListener {
                onClickRoot.invoke(item.id)
            }
        }
    }

    inner class StoryViewHolder(val binding: ItemStoryBinding) : ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}