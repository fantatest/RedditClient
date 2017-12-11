package com.fantadev.redditclient.presentation

import com.fantadev.redditclient.data.model.Reddit

interface RedditTopContract {

    interface View {
        var loading :Boolean

        fun showErrorMessage(message: String)
        fun showEmptyListMarker()
        fun hideEmptyListMarker()
        fun setData(list: List<Reddit>, withRefresh: Boolean)
    }

    interface Presenter {
        fun onCreate(view : View)
        fun onLoadNextPage()
        fun onRefresh()
        //fun getTopReddits()
        fun onDestroy()
    }
}