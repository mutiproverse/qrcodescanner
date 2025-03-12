package com.redlable.qrcodescanner.feature.tabs.browser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import com.redlable.qrcodescanner.databinding.FragmentBrowserBinding
import com.redlable.qrcodescanner.feature.tabs.BottomTabsActivity
import com.redlable.privacybrowser.activities.MainWebViewActivity

class BrowserFragment : Fragment() {

    private lateinit var binding: FragmentBrowserBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* val intent = Intent(requireActivity(), MainWebViewActivity::class.java)
        startActivity(intent)*/

        // Initialize the OnBackPressedCallback
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
           // handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        // Check if you want to navigate back to BottomTabsActivity
        val backIntent = Intent(requireActivity(), BottomTabsActivity::class.java)
        startActivity(backIntent)
        // Optionally, finish the current activity if needed
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the callback when the view is destroyed
        onBackPressedCallback.remove()
    }
}