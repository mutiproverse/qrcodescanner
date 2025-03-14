package com.redlable.qrcodescanner.feature.tabs.create.barcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.redlable.qrcodescanner.databinding.ActivityCreateBarcodeAllBinding
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.feature.BaseActivity
import com.redlable.qrcodescanner.feature.tabs.create.CreateBarcodeActivity
import com.google.zxing.BarcodeFormat

class CreateBarcodeAllActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, CreateBarcodeAllActivity::class.java)
            context.startActivity(intent)
        }
    }
    lateinit var binding: ActivityCreateBarcodeAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBarcodeAllBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportEdgeToEdge()
        handleToolbarBackClicked()
        handleButtonsClicked()
    }

    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }

    private fun handleToolbarBackClicked() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun handleButtonsClicked() {
        binding.buttonDataMatrix.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.DATA_MATRIX) }
        binding.buttonAztec.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.AZTEC) }
        binding.buttonPdf417.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.PDF_417) }
        binding.buttonCodabar.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.CODABAR) }
        binding.buttonCode39.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.CODE_39) }
        binding.buttonCode93.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.CODE_93) }
        binding.buttonCode128.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.CODE_128) }
        binding.buttonEan8.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.EAN_8) }
        binding.buttonEan13.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.EAN_13) }
        binding.buttonItf14.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.ITF) }
        binding.buttonUpcA.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.UPC_A) }
        binding.buttonUpcE.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.UPC_E) }
    }
}