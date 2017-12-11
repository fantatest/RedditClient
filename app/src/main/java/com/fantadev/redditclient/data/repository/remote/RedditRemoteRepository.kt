package com.fantadev.redditclient.data.repository.remote

import com.fantadev.redditclient.data.model.Model
import com.fantadev.redditclient.data.model.Reddit
import com.fantadev.redditclient.data.network.NetworkProvider
import com.fantadev.redditclient.data.repository.RedditRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class RedditRemoteRepository(private val apiService: Retrofit = NetworkProvider.provideRetrofit()) : RedditRepository {

    override fun putRedditList(reddits: List<Reddit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRedditTop(count: Int, limit: Int, after: String?): Observable<Pair<List<Reddit>,String?>> {
        return apiService.create(RedditService::class.java).getTopList(count = count, limit = limit, after = after)
                .flatMap { model ->
                    Observable.zip(
                        Observable.just(model)
                                .flatMapIterable { innerModel -> innerModel.data.children }
                                .map { child -> child.data }
                                .toList()
                                .toObservable(),
                        Observable.just( model.data.after),
                        BiFunction<MutableList<Reddit>, String, Pair<List<Reddit>, String>> { t1, t2 -> Pair(t1,t2) }
                    )
                }

    }



    interface RedditService {

        @GET("r/all/top.json")
        fun getTopList(@Query("count") count: Int, @Query("limit") limit: Int, @Query("after") after: String?) : Observable<Model>
    }
}