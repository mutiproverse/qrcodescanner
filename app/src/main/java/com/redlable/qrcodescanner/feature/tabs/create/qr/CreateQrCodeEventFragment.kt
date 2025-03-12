package com.redlable.qrcodescanner.feature.tabs.create.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redlable.qrcodescanner.databinding.FragmentCreateQrCodeVeventBinding
import com.redlable.qrcodescanner.extension.textString
import com.redlable.qrcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.redlable.qrcodescanner.model.schema.Schema
import com.redlable.qrcodescanner.model.schema.VEvent

class CreateQrCodeEventFragment : BaseCreateBarcodeFragment() {

    private var _binding: FragmentCreateQrCodeVeventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateQrCodeVeventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextTitle.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }

    override fun getBarcodeSchema(): Schema {
        return VEvent(
            uid = binding.editTextTitle.textString,
            organizer = binding.editTextOrganizer.textString,
            summary = binding.editTextSummary.textString,
            startDate = binding.buttonDateTimeStart.dateTime,
            endDate = binding.buttonDateTimeEnd.dateTime
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}