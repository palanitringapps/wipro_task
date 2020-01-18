package com.samplewp.ui.activity


import android.os.Bundle
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

        viewModel.getNewsFeeds(false)

        setAdapter()
        viewModel._feedsModel.observe(this, Observer {
            setFeedsData()
        })


        viewModel._isViewLoading.observe(this, Observer {
            when (it) {
                true -> showProgress()
                false -> hideProgress()
            }
        })

        viewModel._onMessageError.observe(
            this,
            Observer {
                showSnackBar("Network error please try again")
                sr_pull.isRefreshing = false
            })
        sr_pull.setOnRefreshListener {
            sr_pull.isRefreshing = true
            viewModel.getNewsFeeds(true)
        }
    }

    override fun getContentView(): Int = R.layout.activity_main


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            FeedsViewModel.FeedsViewModelFactory(application, FeedRepository())
        ).get(FeedsViewModel::class.java)
    }


    private fun setFeedsData() {
        sr_pull.isRefreshing = false
        feedAdapter.addAll(viewModel.feedResponse.value!!.rows)
    }

    private fun setAdapter() {
        supportActionBar?.title = viewModel._feedsModel.value?.title
        feedAdapter = FeedAdapter()
        rv_feed_list.layoutManager = LinearLayoutManager(this)
        rv_feed_list.adapter = feedAdapter
    }
}