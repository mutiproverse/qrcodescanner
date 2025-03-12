package com.redlable.qrcodescanner.feature.tabs.create.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.redlable.qrcodescanner.databinding.FragmentCreatePdf417Binding
import com.redlable.qrcodescanner.extension.isNotBlank
import com.redlable.qrcodescanner.extension.textString
import com.redlable.qrcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.redlable.qrcodescanner.model.schema.Other
import com.redlable.qrcodescanner.model.schema.Schema

class CreatePdf417Fragment : BaseCreateBarcodeFragment() {

    private var _binding: FragmentCreatePdf417Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePdf417Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.requestFocus()
        binding.editText.addTextChangedListener {
            parentActivity.isCreateBarcodeButtonEnabled = binding.editText.isNotBlank()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBarcodeSchema(): Schema {
        return Other(binding.editText.textString)
    }
}