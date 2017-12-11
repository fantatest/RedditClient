package com.fantadev.redditclient.presentation.adapter

import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fantadev.redditclient.R
import com.fantadev.redditclient.data.model.Reddit
import com.fantadev.redditclient.presentation.utils.Constants
import com.fantadev.redditclient.presentation.utils.RedditListUtilsCallback
import com.fantadev.redditclient.presentation.utils.TimeUtils

class RedditTopAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var redditList : MutableList<Reddit> = mutableListOf()
    private var shouldShowLoading = true

    companion object {
        val ITEM_VIEW_CONTENT = 0
        val ITEM_VIEW_LOADING = 1
    }

    fun setData(list : List<Reddit>, shouldClearPrevious : Boolean = false) {

        val oldReddits = ArrayList(redditList)
        if (shouldClearPrevious) {
            redditList.clear()
        }
        shouldShowLoading != list.isEmpty()
        redditList.addAll(list)
        val diffResult = DiffUtil.calculateDiff(RedditListUtilsCallback(oldReddits, redditList))
        diffResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_CONTENT ->  {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.reddit_top_list_item, parent, false)
                RedditViewHolder(view)
            }
            ITEM_VIEW_LOADING -> {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.loading_item, parent, false)
                LoadingViewHolder(view)
            }
            else -> throw RuntimeException("Adapter ViewType == null")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType ) {
            ITEM_VIEW_CONTENT -> {
                with( holder as RedditViewHolder) {

                    val context = holder.itemView.context
                    val reddit = redditList[position]

                    titleTextView.text = reddit.title
                    authorAndSubredditTextView.text = String.format(context.getString(R.string.author_subreddit), reddit.author, reddit.subreddit)
                    ratingTextView.text = reddit.score.toString()
                    commentsAmountTextView.text = context.resources.getQuantityString(R.plurals.number_comments, reddit.numComments, reddit.numComments)

                    itemView.setOnClickListener({
                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(context, Uri.parse(Constants.baseUrl  + reddit.permalink))
                    })

                    postDateTextView.text = TimeUtils.getTimeAgoStringFromTimestamp(context, reddit.createdUtc * 1000) //WTF - never seen before timestamp without time data

                    Glide.with(context)
                            .load(reddit.thumbnail)
                            .apply(RequestOptions().centerCrop())
                            .error(Glide.with(context)
                                    .load(R.drawable.empty))
                            .into(thumbImageView)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return redditList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && shouldShowLoading) { ITEM_VIEW_LOADING } else { ITEM_VIEW_CONTENT }
    }

    class RedditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ratingTextView : TextView = itemView.findViewById(R.id.reddit_rating_text_view)
        val authorAndSubredditTextView : TextView = itemView.findViewById(R.id.reddit_author_and_subreddit_text_view)
        val titleTextView : TextView = itemView.findViewById(R.id.reddit_title_text_view)
        val commentsAmountTextView : TextView = itemView.findViewById(R.id.reddit_comments_amount_text_view)
        val postDateTextView : TextView = itemView.findViewById(R.id.reddit_post_date)
        val thumbImageView : ImageView = itemView.findViewById(R.id.reddit_thumb_image_view)

    }

    class LoadingViewHolder(loadingView: View) : RecyclerView.ViewHolder(loadingView)
}