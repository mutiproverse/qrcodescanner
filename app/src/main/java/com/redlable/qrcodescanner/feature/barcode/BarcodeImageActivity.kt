package com.redlable.qrcodescanner.feature.barcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.redlable.qrcodescanner.R
import com.redlable.qrcodescanner.databinding.ActivityBarcodeImageBinding
import com.redlable.qrcodescanner.di.barcodeImageGenerator
import com.redlable.qrcodescanner.di.settings
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.extension.toStringId
import com.redlable.qrcodescanner.extension.unsafeLazy
import com.redlable.qrcodescanner.feature.BaseActivity
import com.redlable.qrcodescanner.model.Barcode
import com.redlable.qrcodescanner.usecase.Logger
import java.text.SimpleDateFormat
import java.util.*

class BarcodeImageActivity : BaseActivity() {

    companion object {
        private const val BARCODE_KEY = "BARCODE_KEY"

        fun start(context: Context, barcode: Barcode) {
            val intent = Intent(context, BarcodeImageActivity::class.java)
            intent.putExtra(BARCODE_KEY, barcode)
            context.startActivity(intent)
        }
    }

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH)
    private val barcode by unsafeLazy {
        intent?.getSerializableExtra(BARCODE_KEY) as? Barcode ?: throw IllegalArgumentException("No barcode passed")
    }
    private var originalBrightness: Float = 0.5f
lateinit var binding: ActivityBarcodeImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarcodeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        saveOriginalBrightness()
        handleToolbarBackPressed()
        handleToolbarMenuItemClicked()
        showMenu()
        showBarcode()
    }

    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }

    private fun saveOriginalBrightness() {
        originalBrightness = window.attributes.screenBrightness
    }

    private fun handleToolbarBackPressed() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun handleToolbarMenuItemClicked() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_increase_brightness -> {
                    increaseBrightnessToMax()
                    binding.toolbar.menu.apply {
                        findItem(R.id.item_increase_brightness).isVisible = false
                        findItem(R.id.item_decrease_brightness).isVisible = true
                    }
                }
                R.id.item_decrease_brightness -> {
                    restoreOriginalBrightness()
                    binding.toolbar.menu.apply {
                        findItem(R.id.item_decrease_brightness).isVisible = false
                        findItem(R.id.item_increase_brightness).isVisible = true
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun showMenu() {
        binding.toolbar.inflateMenu(R.menu.menu_barcode_image)
    }

    private fun showBarcode() {
        showBarcodeImage()
        showBarcodeDate()
        showBarcodeFormat()
        showBarcodeText()
    }

    private fun showBarcodeImage() {
        try {
            val bitmap = barcodeImageGenerator.generateBitmap(barcode, 2000, 2000, 0, settings.barcodeContentColor, settings.barcodeBackgroundColor)
            binding.imageViewBarcode.setImageBitmap(bitmap)
            binding.imageViewBarcode.setBackgroundColor(settings.barcodeBackgroundColor)
            binding.layoutBarcodeImageBackground.setBackgroundColor(settings.barcodeBackgroundColor)

            if (settings.isDarkTheme.not() || settings.areBarcodeColorsInversed) {
                binding.layoutBarcodeImageBackground.setPadding(0, 0, 0, 0)
            }
        } catch (ex: Exception) {
            Logger.log(ex)
            binding.imageViewBarcode.isVisible = false
        }
    }

    private fun showBarcodeDate() {
        binding.textViewDate.text = dateFormatter.format(barcode.date)
    }

    private fun showBarcodeFormat() {
        val format = barcode.format.toStringId()
        binding.toolbar.setTitle(format)
    }

    private fun showBarcodeText() {
        binding.textViewBarcodeText.text = barcode.text
    }

    private fun increaseBrightnessToMax() {
        setBrightness(1.0f)
    }

    private fun restoreOriginalBrightness() {
        setBrightness(originalBrightness)
    }

    private fun setBrightness(brightness: Float) {
        window.attributes = window.attributes.apply {
            screenBrightness = brightness
        }
    }
}