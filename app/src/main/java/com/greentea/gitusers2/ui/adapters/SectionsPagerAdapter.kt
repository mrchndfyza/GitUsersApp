package com.greentea.gitusers2.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greentea.gitusers2.ui.fragments.FollowersFragment
import com.greentea.gitusers2.ui.fragments.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username:String = "username"

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(position,username)
            1 -> fragment = FollowingFragment.newInstance(position,username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}