package com.redlable.qrcodescanner.feature.tabs.create.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redlable.qrcodescanner.databinding.FragmentCreateQrCodeMecardBinding
import com.redlable.qrcodescanner.extension.textString
import com.redlable.qrcodescanner.feature.tabs.create.BaseCreateBarcodeFragment
import com.redlable.qrcodescanner.model.Contact
import com.redlable.qrcodescanner.model.schema.MeCard
import com.redlable.qrcodescanner.model.schema.Schema

class CreateQrCodeMeCardFragment : BaseCreateBarcodeFragment() {

    private var _binding: FragmentCreateQrCodeMecardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateQrCodeMecardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextFirstName.requestFocus()
        parentActivity.isCreateBarcodeButtonEnabled = true
    }

    override fun getBarcodeSchema(): Schema {
        return MeCard(
            firstName = binding.editTextFirstName.textString,
            lastName = binding.editTextLastName.textString,
            email = binding.editTextEmail.textString,
            phone = binding.editTextPhone.textString
        )
    }

    override fun showContact(contact: Contact) {
        binding.editTextFirstName.setText(contact.firstName)
        binding.editTextLastName.setText(contact.lastName)
        binding.editTextEmail.setText(contact.email)
        binding.editTextPhone.setText(contact.phone)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}