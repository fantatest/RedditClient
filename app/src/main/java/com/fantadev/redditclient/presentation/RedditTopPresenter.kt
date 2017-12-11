package com.fantadev.redditclient.presentation

import com.fantadev.redditclient.data.cache.InMemoryCache
import com.fantadev.redditclient.data.repository.RedditDataRepository
import com.fantadev.redditclient.data.repository.local.RedditLocalRepository
import com.fantadev.redditclient.data.repository.remote.RedditRemoteRepository
import com.fantadev.redditclient.presentation.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RedditTopPresenter : RedditTopContract.Presenter {

    private var view : RedditTopContract.View? = null
    private val model = RedditDataRepository(RedditLocalRepository(InMemoryCache()), RedditRemoteRepository())
    private var observed = 1
    private var redditAfter: String? = null

    private val subs : CompositeDisposable = CompositeDisposable()

    override fun onCreate(view: RedditTopContract.View) {
        this.view = view
        onLoadNextPage()
    }

    override fun onLoadNextPage() {
        getTopReddits(false)
        view?.loading = true
    }

    override fun onRefresh() {
        observed = 1
        redditAfter = null
        view?.loading = true
        getTopReddits(true)
    }

    private fun getTopReddits(isRefresing:Boolean) {
        subs.add(model.getRedditTop(observed, Constants.itemsInQueryLimit, redditAfter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (reddits, after ) -> view?.setData(reddits, isRefresing)
                    observed += reddits.size
                    redditAfter = after
                    view?.loading = false
                }, {
                   view?.showErrorMessage("Error loading data")
                    view?.loading = false
                }))
    }

    override fun onDestroy() {
        subs.dispose()
        view = null
    }
}