package com.redlable.qrcodescanner.feature.common.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isInvisible
import com.redlable.qrcodescanner.R

class SettingsRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var radio_button: RadioButton
    private lateinit var text_view_text: TextView
    private lateinit var delimiter: View

    var isChecked: Boolean
        get() = radio_button.isChecked
        set(value) {
            radio_button.isChecked = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_settings_radio_button, this, true)
        radio_button = findViewById(R.id.radio_button)
        text_view_text = findViewById(R.id.text_view_text)
        delimiter = findViewById(R.id.delimiter)

        context.obtainStyledAttributes(attrs, R.styleable.SettingsRadioButton).apply {
            showText(this)
            showDelimiter(this)
            recycle()
        }

        setOnClickListener {
            radio_button.toggle()
        }
    }

    fun setCheckedChangedListener(listener: ((Boolean) -> Unit)?) {
        radio_button.setOnCheckedChangeListener { _, isChecked ->
            listener?.invoke(isChecked)
        }
    }

    private fun showText(attributes: TypedArray) {
        text_view_text.text = attributes.getString(R.styleable.SettingsRadioButton_text).orEmpty()
    }

    private fun showDelimiter(attributes: TypedArray) {
        delimiter.isInvisible =
            attributes.getBoolean(R.styleable.SettingsRadioButton_isDelimiterVisible, true).not()
    }
}