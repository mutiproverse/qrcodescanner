/*
 * Copyright © 2016-2023 Soren Stoutner <soren@stoutner.com>.
 *
 * This file is part of Privacy Browser Android <https://www.stoutner.com/privacy-browser-android>.
 *
 * Privacy Browser Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Privacy Browser Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Privacy Browser Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redlable.privacybrowser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.redlable.privacybrowser.fragments.AboutVersionFragment
import com.redlable.privacybrowser.fragments.AboutWebViewFragment

class AboutStateAdapter(fragmentActivity: FragmentActivity, private val filterListVersions: Array<String>) : FragmentStateAdapter(fragmentActivity) {
    // Get the number of tabs.
    override fun getItemCount(): Int {
        // There are seven tabs.
        return 7
    }

    // Create the tab.
    override fun createFragment(tabNumber: Int): Fragment {
        // Return the tab fragment.
        return if (tabNumber == 0)  // Return the about tab fragment.
            AboutVersionFragment.createTab(filterListVersions)
        else  // Return a WebView tab.
            AboutWebViewFragment.createTab(tabNumber)
    }
}
