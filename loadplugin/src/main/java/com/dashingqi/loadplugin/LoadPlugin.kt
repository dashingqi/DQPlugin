package com.dashingqi.loadplugin

import android.util.Log

/**
 * @author zhangqi61
 * @since 2021/11/22
 */
class LoadPlugin {

    fun performClick() {
        kotlin.runCatching {

        }.getOrNull()
        Log.d(TAG, "performClick: ")
    }

    companion object {
        private const val TAG = "LoadPlugin"
    }
}