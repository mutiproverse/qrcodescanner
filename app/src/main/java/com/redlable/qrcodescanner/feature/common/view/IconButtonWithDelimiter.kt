package com.redlable.qrcodescanner.feature.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.redlable.qrcodescanner.R
import com.redlable.qrcodescanner.databinding.LayoutIconButtonWithDelimiterBinding

class IconButtonWithDelimiter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutIconButtonWithDelimiterBinding

    init {
        binding = LayoutIconButtonWithDelimiterBinding.inflate(LayoutInflater.from(context), this, true)
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.IconButtonWithDelimiter).apply {
            showIcon(this)
            showIconBackgroundColor(this)
            showText(this)
            showArrow(this)
            showDelimiter(this)
            recycle()
        }
    }

    private fun showIcon(attributes: TypedArray) {
        val iconResId = attributes.getResourceId(R.styleable.IconButtonWithDelimiter_icon, -1)
        val icon = AppCompatResources.getDrawable(context, iconResId)
        binding.imageViewSchema.setImageDrawable(icon)
    }

    private fun showIconBackgroundColor(attributes: TypedArray) {
        val color = attributes.getColor(R.styleable.IconButtonWithDelimiter_iconBackground, context.resources.getColor(R.color.green))
        (binding.layoutImage.background.mutate() as GradientDrawable).setColor(color)
    }

    private fun showText(attributes: TypedArray) {
        binding.textView.text = attributes.getString(R.styleable.IconButtonWithDelimiter_text).orEmpty()
    }

    private fun showArrow(attributes: TypedArray) {
        binding.imageViewArrow.isVisible = attributes.getBoolean(R.styleable.IconButtonWithDelimiter_isArrowVisible, false)
    }

    private fun showDelimiter(attributes: TypedArray) {
        binding.delimiter.isInvisible = attributes.getBoolean(R.styleable.IconButtonWithDelimiter_isDelimiterVisible, true).not()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.imageViewSchema.isEnabled = enabled
        binding.textView.isEnabled = enabled
    }
}