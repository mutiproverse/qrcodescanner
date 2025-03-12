package com.redlable.qrcodescanner.feature.tabs.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.redlable.qrcodescanner.R
import com.redlable.qrcodescanner.databinding.FragmentCreateBarcodeBinding
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.extension.clipboardManager
import com.redlable.qrcodescanner.extension.orZero
import com.redlable.qrcodescanner.model.schema.BarcodeSchema

class CreateBarcodeFragment : Fragment() {

    lateinit var binding: FragmentCreateBarcodeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCreateBarcodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportEdgeToEdge()
        handleButtonsClicked()
        binding.scrollViewBarcode.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        binding.qrCodeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple)

        binding.qrCodeButton.setOnClickListener{
            binding.scrollViewBarcode.visibility = View.GONE
            binding.qrCodeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple)
            binding.barCodeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray10)
            binding.scrollView.visibility = View.VISIBLE

        }

        binding.barCodeButton.setOnClickListener{
            binding.scrollView.visibility = View.GONE
            binding.scrollViewBarcode.visibility = View.VISIBLE
            binding.qrCodeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray10)
            binding.barCodeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple)


        }
    }

    private fun supportEdgeToEdge() {
        binding.appBarLayout.applySystemWindowInsets(applyTop = true)
    }

    private fun handleButtonsClicked() {
        // QR code
        binding.buttonClipboard.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.OTHER, getClipboardContent())  }
        binding.buttonText.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.OTHER) }
        binding.buttonUrl.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.URL) }
        binding.buttonWifi.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.WIFI) }
        binding.buttonLocation.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.GEO) }
        binding.buttonOtp.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.OTP_AUTH) }
        binding.buttonContactVcard.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.VCARD) }
        binding.buttonContactMecard.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.MECARD) }
        binding.buttonEvent.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.VEVENT) }
        binding.buttonPhone.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.PHONE) }
        binding.buttonEmail.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.EMAIL) }
        binding.buttonSms.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.SMS) }
        binding.buttonMms.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.MMS) }
        binding.buttonCryptocurrency.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.CRYPTOCURRENCY) }
        binding.buttonBookmark.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.BOOKMARK) }
        binding.buttonApp.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.APP) }
//        binding.buttonShowAllQrCode.setOnClickListener { CreateQrCodeAllActivity.start(requireActivity()) }

        // Barcode
//        binding.buttonCreateBarcode.setOnClickListener { CreateBarcodeAllActivity.start(requireActivity()) }
        binding.buttonDataMatrix.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.DATA_MATRIX) }
        binding.buttonAztec.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.AZTEC) }
        binding.buttonPdf417.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.PDF_417) }
        binding.buttonCodabar.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.CODABAR) }
        binding.buttonCode39.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.CODE_39) }
        binding.buttonCode93.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.CODE_93) }
        binding.buttonCode128.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.CODE_128) }
        binding.buttonEan8.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.EAN_8) }
        binding.buttonEan13.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.EAN_13) }
        binding.buttonItf14.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.ITF) }
        binding.buttonUpcA.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.UPC_A) }
        binding.buttonUpcE.setOnClickListener { CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.UPC_E) }
    }

    private fun getClipboardContent(): String {
        val clip = requireActivity().clipboardManager?.primaryClip ?: return ""
        return when (clip.itemCount.orZero()) {
            0 -> ""
            else -> clip.getItemAt(0).text.toString()
        }
    }
}