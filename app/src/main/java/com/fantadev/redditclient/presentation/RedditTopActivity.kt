package com.fantadev.redditclient.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.fantadev.redditclient.R
import com.fantadev.redditclient.data.model.Reddit
import com.fantadev.redditclient.presentation.adapter.RedditTopAdapter
import kotlinx.android.synthetic.main.activity_reddit_top.*
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View


class RedditTopActivity : AppCompatActivity(), RedditTopContract.View {

    private val presenter = RedditTopPresenter()
    private val adapter =  RedditTopAdapter()

    override var loading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit_top)

        reddit_top_recycler_view.adapter = adapter
        reddit_top_recycler_view.layoutManager = LinearLayoutManager(this)
        reddit_top_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        reddit_top_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerView.layoutManager.childCount
                val totalItemCount = recyclerView.layoutManager.itemCount
                val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (!reddit_top_swipe_refresh_layout.isRefreshing && !loading/*&& !mEndOfList*/) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 2
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 25) {
                        presenter.onLoadNextPage()
                    }
                }
            }
        })
        presenter.onCreate(this)

        reddit_top_swipe_refresh_layout.setOnRefreshListener {
            reddit_top_swipe_refresh_layout.isRefreshing = true
            presenter.onRefresh()
        }
    }

    override fun setData(list: List<Reddit>, withRefresh :Boolean) {
        reddit_top_swipe_refresh_layout.isRefreshing = false
        adapter.setData(list, withRefresh)
        if (adapter.itemCount == 0) {
            showEmptyListMarker()
        } else {
            hideEmptyListMarker()
        }
    }

    override fun showEmptyListMarker() {
       no_items_marker.visibility = View.VISIBLE
    }

    override fun hideEmptyListMarker() {
        no_items_marker.visibility = View.INVISIBLE
    }

    override fun showErrorMessage(message: String) {
        reddit_top_swipe_refresh_layout.isRefreshing = false
        if (adapter.itemCount == 0) {
            showEmptyListMarker()
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
