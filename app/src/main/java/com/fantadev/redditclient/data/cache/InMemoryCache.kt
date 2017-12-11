package com.fantadev.redditclient.data.cache

import com.fantadev.redditclient.data.model.Reddit
import io.reactivex.Observable

class InMemoryCache : LocalCache {

    private val reddits = mutableListOf<Reddit>()

    override fun getRedditList(limit: Int, count : Int): Observable<List<Reddit>> {
        val cacheSize = reddits.size
        return Observable.just(reddits.subList( if(count >= cacheSize) {
            cacheSize
        } else {
            count
        },
                if (limit + count >= cacheSize) {
                    val amount = cacheSize - count
                    if ( amount >= 0 ) { amount } else 0
                } else {
                    limit
                })
        )
    }

    override fun putRedditList(list: List<Reddit>) {
        reddits.union(list)
    }

}