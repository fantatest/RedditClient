package com.fantadev.redditclient.data.repository

import android.util.Log
import com.fantadev.redditclient.data.model.Reddit
import com.fantadev.redditclient.data.repository.local.RedditLocalRepository
import com.fantadev.redditclient.data.repository.remote.RedditRemoteRepository
import io.reactivex.Observable

class RedditDataRepository(private val local: RedditLocalRepository, private val remote: RedditRemoteRepository) : RedditRepository {

    override fun getRedditTop(count:Int, limit:Int, after: String?) : Observable<Pair<List<Reddit>,String?>> {
        return /*Observable.concat(*//*local.getRedditTop(count, limit, after), */remote.getRedditTop(count, limit, after)
                .doOnNext{ (reddits, _) -> local.putRedditList(reddits)
                    Log.d("log", reddits.size.toString())
                }/*)*/
    }

    override fun putRedditList(reddits: List<Reddit>) {
        local.putRedditList(reddits)
    }
}