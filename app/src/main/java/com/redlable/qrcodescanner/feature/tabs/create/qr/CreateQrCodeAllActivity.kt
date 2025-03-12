package com.redlable.qrcodescanner.feature.tabs.create.qr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.redlable.qrcodescanner.databinding.ActivityCreateQrCodeAllBinding
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.feature.BaseActivity
import com.redlable.qrcodescanner.feature.tabs.create.CreateBarcodeActivity
import com.redlable.qrcodescanner.model.schema.BarcodeSchema
import com.google.zxing.BarcodeFormat

class CreateQrCodeAllActivity : BaseActivity() {

    private var _binding: ActivityCreateQrCodeAllBinding? = null
    private val binding get() = _binding!!

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, CreateQrCodeAllActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateQrCodeAllBinding.inflate(layoutInflater)
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
        binding.buttonText.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.OTHER) }
        binding.buttonUrl.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.URL) }
        binding.buttonWifi.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.WIFI) }
        binding.buttonLocation.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.GEO) }
        binding.buttonOtp.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.OTP_AUTH) }
        binding.buttonContactVcard.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.VCARD) }
        binding.buttonContactMecard.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.MECARD) }
        binding.buttonEvent.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.VEVENT) }
        binding.buttonPhone.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.PHONE) }
        binding.buttonEmail.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.EMAIL) }
        binding.buttonSms.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.SMS) }
        binding.buttonMms.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.MMS) }
        binding.buttonCryptocurrency.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.CRYPTOCURRENCY) }
        binding.buttonBookmark.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.BOOKMARK) }
        binding.buttonApp.setOnClickListener { CreateBarcodeActivity.start(this, BarcodeFormat.QR_CODE, BarcodeSchema.APP) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}