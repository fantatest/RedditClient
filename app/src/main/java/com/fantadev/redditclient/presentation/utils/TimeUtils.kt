package com.fantadev.redditclient.presentation.utils

import android.content.Context
import com.fantadev.redditclient.R
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun getTimeAgoStringFromTimestamp(context : Context, timestamp: Long) : String {

        //TODO: optimize

        val timeGap = Date().time - timestamp

        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeGap)
        val hours = TimeUnit.MILLISECONDS.toHours(timeGap)
        val days = TimeUnit.MILLISECONDS.toDays(timeGap)

        return if (minutes >= 60 && hours >= 24) {
            context.resources.getQuantityString(R.plurals.days_ago, days.toInt(), days.toInt())
        } else if (minutes >= 60) {
            context.resources.getQuantityString(R.plurals.hours_ago, hours.toInt(), hours.toInt())
        } else {
            context.resources.getQuantityString(R.plurals.minutes_ago, minutes.toInt(), minutes.toInt())
        }
    }
}