package com.fantadev.redditclient.data.repository.local

import com.fantadev.redditclient.data.cache.LocalCache
import com.fantadev.redditclient.data.model.Reddit
import com.fantadev.redditclient.data.repository.RedditRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class RedditLocalRepository(private val cache: LocalCache) : RedditRepository {

    override fun putRedditList(reddits: List<Reddit>) {
        cache.putRedditList(reddits)
    }

    override fun getRedditTop(count: Int, limit: Int, after : String?): Observable<Pair<List<Reddit>, String?>> {
        return Observable.zip(cache.getRedditList(limit, count), Observable.just(""), BiFunction {
            reddits, after -> Pair(reddits, null)
        })
    }
}