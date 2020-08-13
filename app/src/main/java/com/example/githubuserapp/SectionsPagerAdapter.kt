package com.example.githubuserapp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(
    private val mContext: Context,
    fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var username: String? = null
    private val tabTitles = intArrayOf(R.string.tab_followers,R.string.tab_following)
    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> return FollowersFragment.newInstances(username)
            1 -> return FollowingFragment.newInstances(username)
        }
        return Fragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
    override fun getCount(): Int {
        return 2
    }

}