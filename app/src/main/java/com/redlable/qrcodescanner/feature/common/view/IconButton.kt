package com.redlable.qrcodescanner.feature.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.redlable.qrcodescanner.R

class IconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var text_view: TextView
    private lateinit var image_view_schema: ImageView
    private lateinit var layout_image: FrameLayout

    var text: String
        get() = text_view.text.toString()
        set(value) {
            text_view.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_icon_button, this, true)
        text_view = findViewById(R.id.text_view)
        image_view_schema = findViewById(R.id.image_view_schema)
        layout_image = findViewById(R.id.layout_image)

        context.obtainStyledAttributes(attrs, R.styleable.IconButton).apply {
            showIcon(this)
            showIconBackgroundColor(this)
            showText(this)
            recycle()
        }
    }

    private fun showIcon(attributes: TypedArray) {
        val iconResId = attributes.getResourceId(R.styleable.IconButton_icon, -1)
        if (iconResId != -1) {
            val icon = AppCompatResources.getDrawable(context, iconResId)
            image_view_schema.setImageDrawable(icon)
        }
    }

    private fun showIconBackgroundColor(attributes: TypedArray) {
        val color = attributes.getColor(
            R.styleable.IconButton_iconBackground,
            ContextCompat.getColor(context, R.color.green)
        )
        val background = layout_image.background
        if (background is GradientDrawable) {
            background.setColor(color)
        }
    }

    private fun showText(attributes: TypedArray) {
        text_view.text = attributes.getString(R.styleable.IconButton_text).orEmpty()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        image_view_schema.isEnabled = enabled
        text_view.isEnabled = enabled
    }
}