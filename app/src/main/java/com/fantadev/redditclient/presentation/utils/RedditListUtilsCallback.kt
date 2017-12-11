package com.fantadev.redditclient.presentation.utils

import android.support.v7.util.DiffUtil
import com.fantadev.redditclient.data.model.Reddit

class RedditListUtilsCallback(
        private val oldList : List<Reddit> = mutableListOf<Reddit>(),
        private val newList : List<Reddit> = mutableListOf<Reddit>()) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}