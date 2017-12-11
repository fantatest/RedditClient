package com.fantadev.redditclient.data.cache

import com.fantadev.redditclient.data.model.Reddit
import io.reactivex.Observable

interface LocalCache {

    fun getRedditList( limit: Int, count: Int) : Observable<List<Reddit>>
    fun putRedditList( list: List<Reddit>)
}