package com.samplewp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samplewp.R
import com.samplewp.base.BaseViewHolder
import com.samplewp.databinding.AdapterFeedItemBinding
import com.samplewp.model.Row

class FeedAdapter(rows: ArrayList<Row>) :
    RecyclerView.Adapter<FeedViewHolder>() {
    var feedRow: ArrayList<Row>? = null

    init {
        feedRow = rows
    }

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
        return feedRow!!.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.onBind(feedRow!![position])
    }


    fun addAll(rows: ArrayList<Row>) {
        feedRow = rows
        notifyDataSetChanged()
    }
}

class FeedViewHolder(private val binding: AdapterFeedItemBinding) :
    BaseViewHolder(binding) {
    fun onBind(feed: Row?) {
        binding.model = feed
        binding.tvDescription.text = value(feed!!.description)
        binding.tvTitle.text = value(feed!!.title)
        Glide.with(binding.ivLogo.context).load(feed?.imageHref).placeholder(R.drawable.ic_default)
            .into(binding.ivLogo)
        binding.executePendingBindings()
    }

    private fun value(text: String): String = if (text.isNullOrEmpty()) "NA" else text
}