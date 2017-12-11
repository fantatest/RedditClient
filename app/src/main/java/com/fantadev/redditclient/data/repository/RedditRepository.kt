package com.fantadev.redditclient.data.repository

import com.fantadev.redditclient.data.model.Reddit
import io.reactivex.Observable

interface RedditRepository {

    fun getRedditTop(count: Int, limit: Int, after: String?): Observable<Pair<List<Reddit>,String?>>
    fun putRedditList(reddits: List<Reddit>)
}