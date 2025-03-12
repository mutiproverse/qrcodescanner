package com.redlable.qrcodescanner.feature.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context?, attrs: AttributeSet?) :
    ViewPager(context!!, attrs) {

    private var mIsEnabled = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mIsEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return try{
            if (mIsEnabled) {
                super.onInterceptTouchEvent(event)
            } else false
        }catch (e: Exception){
            false
        }
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.mIsEnabled = enabled
    }

    interface DisableViewPager {
        fun disablePager(disable:Boolean)
    }

}