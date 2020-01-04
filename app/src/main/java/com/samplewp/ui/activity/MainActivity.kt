package com.samplewp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.samplewp.R
import com.samplewp.base.BaseActivity
import com.samplewp.repo.FeedRepository
import com.samplewp.ui.adapter.FeedAdapter
import com.samplewp.viewmodel.FeedsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ViewDataBinding>() {
    private lateinit var viewModel: FeedsViewModel
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

        viewModel._sampleResponse.observe(this, Observer { setAdapter() })

        viewModel._onMessageError.observe(
            this,
            Observer {
                Toast.makeText(this, "Network error please try again", Toast.LENGTH_SHORT).show()
            })
        sr_pull.setOnRefreshListener {
            sr_pull.isRefreshing = true
            viewModel.refreshFeed()
        }
    }

    override fun getContentView(): Int = R.layout.activity_main


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            FeedsViewModel.FeedsViewModelFactory(application, FeedRepository())
        ).get(FeedsViewModel::class.java)
    }


    private fun setAdapter() {
        if (sr_pull.isRefreshing) {
            sr_pull.isRefreshing = false
            feedAdapter.clear()
            feedAdapter.addAll(viewModel._sampleResponse.value!!)
            return
        }
        supportActionBar?.title = viewModel._sampleResponse.value?.title
        feedAdapter = FeedAdapter(viewModel._sampleResponse.value!!)
        rv_feed_list.layoutManager = LinearLayoutManager(this)
        rv_feed_list.adapter = feedAdapter
    }
}