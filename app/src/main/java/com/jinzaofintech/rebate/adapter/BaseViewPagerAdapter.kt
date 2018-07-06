package com.jinzaofintech.rebate.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by zengwendi on 2017/12/12.
 * the standard viewpager adapter
 */
class BaseViewPagerAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}