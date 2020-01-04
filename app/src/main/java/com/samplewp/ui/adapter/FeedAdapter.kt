package com.samplewp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samplewp.R
import com.samplewp.base.BaseViewHolder
import com.samplewp.databinding.AdapterFeedItemBinding
import com.samplewp.model.Row
import com.samplewp.model.SampleResponse

class FeedAdapter(var feed: SampleResponse) :
    RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            AdapterFeedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return feed.rows.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.onBind(feed.rows[position])
    }

    fun clear() {
        feed.rows.clear()
        notifyDataSetChanged()
    }

    fun addAll(newFeeds: SampleResponse) {
        feed = newFeeds
        notifyDataSetChanged()
    }
}

class FeedViewHolder(private val binding: AdapterFeedItemBinding) :
    BaseViewHolder(binding) {
    fun onBind(feed: Row?) {
        binding.model = feed
        Glide.with(binding.ivLogo.context).load(feed?.imageHref).placeholder(R.drawable.ic_default)
            .into(binding.ivLogo)
        binding.executePendingBindings()
    }
}