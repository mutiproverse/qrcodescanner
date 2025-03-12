/*
 * Copyright 2016-2024 Soren Stoutner <soren@stoutner.com>.
 *
 * This file is part of Privacy Browser Android <https://www.stoutner.com/privacy-browser-android/>.
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

package com.redlable.privacybrowser.activities

import android.os.Bundle
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.redlable.privacybrowser.R
import com.redlable.privacybrowser.adapters.GuideStateAdapter

class GuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Get a handle for the shared preferences.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Get the preferences.
        val allowScreenshots = sharedPreferences.getBoolean(getString(R.string.allow_screenshots_key), false)
        val bottomAppBar = sharedPreferences.getBoolean(getString(R.string.bottom_app_bar_key), false)

        // Disable screenshots if not allowed.
        if (!allowScreenshots)
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        // Run the default commands.
        super.onCreate(savedInstanceState)

        // Set the content view.
        if (bottomAppBar)
            setContentView(R.layout.guide_bottom_appbar)
        else
            setContentView(R.layout.guide_top_appbar)

        // Get handles for the views.
        val toolbar = findViewById<Toolbar>(R.id.guide_toolbar)
        val guideTabLayout = findViewById<TabLayout>(R.id.guide_tablayout)
        val guideViewPager2 = findViewById<ViewPager2>(R.id.guide_viewpager2)

        // Set the support action bar.
        setSupportActionBar(toolbar)

        // Get a handle for the action bar.
        val actionBar = supportActionBar!!

        // Display the home arrow on the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true)

        // Initialize the guide state adapter.
        val guideStateAdapter = GuideStateAdapter(this)

        // Set the view pager adapter.
        guideViewPager2.adapter = guideStateAdapter

        // Disable swiping between pages in the view pager.
        guideViewPager2.isUserInputEnabled = false

        // Create a tab layout mediator.  Tab numbers start at 0.
        TabLayoutMediator(guideTabLayout, guideViewPager2) { tab, position ->
            // Set the tab text based on the position.
            tab.text = when (position) {
                0 -> getString(R.string.overview)
                1 -> getString(R.string.javascript)
                2 -> getString(R.string.local_storage)
                3 -> getString(R.string.user_agent)
                4 -> getString(R.string.requests)
                5 -> getString(R.string.domain_settings)
                6 -> getString(R.string.ssl_certificates)
                7 -> getString(R.string.proxies)
                8 -> getString(R.string.tracking_ids)
                9 -> getString(R.string.gui)
                else -> ""
            }
        }.attach()
    }
}
