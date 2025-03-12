package com.redlable.qrcodescanner.feature.tabs.settings.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redlable.qrcodescanner.databinding.ActivityChooseSearchEngineBinding
import com.redlable.qrcodescanner.di.settings
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.extension.unsafeLazy
import com.redlable.qrcodescanner.feature.BaseActivity
import com.redlable.qrcodescanner.feature.common.view.SettingsRadioButton
import com.redlable.qrcodescanner.model.SearchEngine

class ChooseSearchEngineActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChooseSearchEngineActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityChooseSearchEngineBinding
    private val buttons by unsafeLazy {
        listOf(
            binding.buttonNone,
            binding.buttonAskEveryTime,
            binding.buttonBing,
            binding.buttonDuckDuckGo,
            binding.buttonGoogle,
            binding.buttonQwant,
            binding.buttonStartpage,
            binding.buttonYahoo,
            binding.buttonYandex
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSearchEngineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        initToolbar()
        showInitialValue()
        handleSettingsChanged()
    }

    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun showInitialValue() {
        when (settings.searchEngine) {
            SearchEngine.NONE -> binding.buttonNone.isChecked = true
            SearchEngine.ASK_EVERY_TIME -> binding.buttonAskEveryTime.isChecked = true
            SearchEngine.BING -> binding.buttonBing.isChecked = true
            SearchEngine.DUCK_DUCK_GO -> binding.buttonDuckDuckGo.isChecked = true
            SearchEngine.GOOGLE -> binding.buttonGoogle.isChecked = true
            SearchEngine.QWANT -> binding.buttonQwant.isChecked = true
            SearchEngine.STARTPAGE -> binding.buttonStartpage.isChecked = true
            SearchEngine.YAHOO -> binding.buttonYahoo.isChecked = true
            SearchEngine.YANDEX -> binding.buttonYandex.isChecked = true
        }
    }

    private fun handleSettingsChanged() {
        binding.buttonNone.setCheckedChangedListener(SearchEngine.NONE)
        binding.buttonAskEveryTime.setCheckedChangedListener(SearchEngine.ASK_EVERY_TIME)
        binding.buttonBing.setCheckedChangedListener(SearchEngine.BING)
        binding.buttonDuckDuckGo.setCheckedChangedListener(SearchEngine.DUCK_DUCK_GO)
        binding.buttonGoogle.setCheckedChangedListener(SearchEngine.GOOGLE)
        binding.buttonQwant.setCheckedChangedListener(SearchEngine.QWANT)
        binding.buttonStartpage.setCheckedChangedListener(SearchEngine.STARTPAGE)
        binding.buttonYahoo.setCheckedChangedListener(SearchEngine.YAHOO)
        binding.buttonYandex.setCheckedChangedListener(SearchEngine.YANDEX)
    }

    private fun SettingsRadioButton.setCheckedChangedListener(searchEngine: SearchEngine) {
        setCheckedChangedListener { isChecked ->
            if (isChecked) {
                uncheckOtherButtons(this)
                settings.searchEngine = searchEngine
            }
        }
    }

    private fun uncheckOtherButtons(checkedButton: View) {
        buttons.forEach { button ->
            if (checkedButton !== button) {
                button.isChecked = false
            }
        }
    }
}