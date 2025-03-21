/*
 * Copyright 2016-2024 Soren Stoutner <soren@stoutner.com>.
 *
 * This file is part of Privacy Browser Android <https://www.stoutner.com/privacy-browser-android>.
 *
 * Privacy Browser Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Privacy Browser Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Privacy Browser Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redlable.privacybrowser.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.cursoradapter.widget.ResourceCursorAdapter
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager

import com.google.android.material.snackbar.Snackbar

import com.redlable.privacybrowser.R
import com.redlable.privacybrowser.activities.HOME_FOLDER_DATABASE_ID
import com.redlable.privacybrowser.activities.HOME_FOLDER_ID
import com.redlable.privacybrowser.helpers.BOOKMARK_NAME
import com.redlable.privacybrowser.helpers.BOOKMARK_URL
import com.redlable.privacybrowser.helpers.DISPLAY_ORDER
import com.redlable.privacybrowser.helpers.FAVORITE_ICON
import com.redlable.privacybrowser.helpers.FOLDER_ID
import com.redlable.privacybrowser.helpers.ID
import com.redlable.privacybrowser.helpers.PARENT_FOLDER_ID
import com.redlable.privacybrowser.helpers.BookmarksDatabaseHelper

import java.io.ByteArrayOutputStream

// Define the class constants.
private const val DATABASE_ID = "A"
private const val FAVORITE_ICON_BYTE_ARRAY = "B"

class EditBookmarkDatabaseViewDialog : DialogFragment() {
    companion object {
        fun bookmarkDatabaseId(databaseId: Int, favoriteIconBitmap: Bitmap): EditBookmarkDatabaseViewDialog {
            // Create a favorite icon byte array output stream.
            val favoriteIconByteArrayOutputStream = ByteArrayOutputStream()

            // Convert the favorite icon to a PNG and place it in the byte array output stream.  `0` is for lossless compression (the only option for a PNG).
            favoriteIconBitmap.compress(Bitmap.CompressFormat.PNG, 0, favoriteIconByteArrayOutputStream)

            // Convert the byte array output stream to a byte array.
            val favoriteIconByteArray = favoriteIconByteArrayOutputStream.toByteArray()

            // Create an arguments bundle.
            val argumentsBundle = Bundle()

            // Store the variables in the bundle.
            argumentsBundle.putInt(DATABASE_ID, databaseId)
            argumentsBundle.putByteArray(FAVORITE_ICON_BYTE_ARRAY, favoriteIconByteArray)

            // Create a new instance of the dialog.
            val editBookmarkDatabaseViewDialog = EditBookmarkDatabaseViewDialog()

            // Add the arguments bundle to the dialog.
            editBookmarkDatabaseViewDialog.arguments = argumentsBundle

            // Return the new dialog.
            return editBookmarkDatabaseViewDialog
        }
    }

    private val browseActivityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
        // Only do something if the user didn't press back from the file picker.
        if (imageUri != null) {
            // Get a handle for the content resolver.
            val contentResolver = requireContext().contentResolver

            // Get the image MIME type.
            val mimeType = contentResolver.getType(imageUri)

            // Decode the image according to the type.
            if (mimeType == "image/svg+xml") {  // The image is an SVG.
                // Display a snackbar.
                Snackbar.make(bookmarkNameEditText, getString(R.string.cannot_use_svg), Snackbar.LENGTH_LONG).show()
            } else {  // The image is not an SVG.
                // Get an input stream for the image URI.
                val inputStream = contentResolver.openInputStream(imageUri)

                // Get the bitmap from the URI.
                // `ImageDecoder.decodeBitmap` can't be used, because when running `Drawable.toBitmap` later the `Software rendering doesn't support hardware bitmaps` error message might be produced.
                var imageBitmap = BitmapFactory.decodeStream(inputStream)

                // Scale the image down if it is greater than 128 pixels in either direction.
                if ((imageBitmap != null) && ((imageBitmap.height > 128) || (imageBitmap.width > 128)))
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 128, 128, true)

                // Display the new custom favorite icon.
                customIconImageView.setImageBitmap(imageBitmap)

                // Select the custom icon radio button.
                customIconLinearLayout.performClick()
            }
        }
    }

    // Declare the class views.
    private lateinit var bookmarkNameEditText: EditText
    private lateinit var bookmarkUrlEditText: EditText
    private lateinit var currentIconRadioButton: RadioButton
    private lateinit var customIconImageView: ImageView
    private lateinit var customIconLinearLayout: LinearLayout
    private lateinit var displayOrderEditText: EditText
    private lateinit var folderSpinner: Spinner
    private lateinit var saveButton: Button

    // Declare the class variables.
    private lateinit var editBookmarkDatabaseViewListener: EditBookmarkDatabaseViewListener

    // The public interface is used to send information back to the parent activity.
    interface EditBookmarkDatabaseViewListener {
        fun saveBookmark(dialogFragment: DialogFragment, selectedBookmarkDatabaseId: Int)
    }

    override fun onAttach(context: Context) {
        // Run the default commands.
        super.onAttach(context)

        // Get a handle for edit bookmark database view listener from the launching context.
        editBookmarkDatabaseViewListener = context as EditBookmarkDatabaseViewListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Get the arguments.
        val arguments = requireArguments()

        // Get the variables from the arguments.
        val bookmarkDatabaseId = arguments.getInt(DATABASE_ID)
        val favoriteIconByteArray = arguments.getByteArray(FAVORITE_ICON_BYTE_ARRAY)!!

        // Convert the favorite icon byte array to a bitmap.
        val favoriteIconBitmap = BitmapFactory.decodeByteArray(favoriteIconByteArray, 0, favoriteIconByteArray.size)

        // Initialize the database helper.
        val bookmarksDatabaseHelper = BookmarksDatabaseHelper(requireContext())

        // Get a cursor with the selected bookmark.
        val bookmarkCursor = bookmarksDatabaseHelper.getBookmark(bookmarkDatabaseId)

        // Move the cursor to the first position.
        bookmarkCursor.moveToFirst()

        // Use an alert dialog builder to create the dialog and set the style according to the theme.
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.PrivacyBrowserAlertDialog)

        // Set the title.
        dialogBuilder.setTitle(R.string.edit_bookmark)

        // Set the icon.
        dialogBuilder.setIcon(R.drawable.bookmark)

        // Set the view.
        dialogBuilder.setView(R.layout.edit_bookmark_databaseview_dialog)

        // Set the cancel button listener.  Using `null` as the listener closes the dialog without doing anything else.
        dialogBuilder.setNegativeButton(R.string.cancel, null)

        // Set the save button listener.
        dialogBuilder.setPositiveButton(R.string.save) { _: DialogInterface, _: Int ->
            // Return the dialog fragment to the parent activity on save.
            editBookmarkDatabaseViewListener.saveBookmark(this, bookmarkDatabaseId)
        }

        // Create an alert dialog from the alert dialog builder.
        val alertDialog = dialogBuilder.create()

        // Get a handle for the shared preferences.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // Get the screenshot preference.
        val allowScreenshots = sharedPreferences.getBoolean(getString(R.string.allow_screenshots_key), false)

        // Disable screenshots if not allowed.
        if (!allowScreenshots) {
            alertDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        // The alert dialog must be shown before items in the layout can be modified.
        alertDialog.show()

        // Get handles for the layout items.
        val databaseIdTextView = alertDialog.findViewById<TextView>(R.id.bookmark_database_id_textview)!!
        val currentIconLinearLayout = alertDialog.findViewById<LinearLayout>(R.id.current_icon_linearlayout)!!
        currentIconRadioButton = alertDialog.findViewById(R.id.current_icon_radiobutton)!!
        val currentIconImageView = alertDialog.findViewById<ImageView>(R.id.current_icon_imageview)!!
        val webpageFavoriteIconLinearLayout = alertDialog.findViewById<LinearLayout>(R.id.webpage_favorite_icon_linearlayout)!!
        val webpageFavoriteIconRadioButton = alertDialog.findViewById<RadioButton>(R.id.webpage_favorite_icon_radiobutton)!!
        val webpageFavoriteIconImageView = alertDialog.findViewById<ImageView>(R.id.webpage_favorite_icon_imageview)!!
        customIconLinearLayout = alertDialog.findViewById(R.id.custom_icon_linearlayout)!!
        val customIconRadioButton = alertDialog.findViewById<RadioButton>(R.id.custom_icon_radiobutton)!!
        customIconImageView = alertDialog.findViewById(R.id.custom_icon_imageview)!!
        val browseButton = alertDialog.findViewById<Button>(R.id.browse_button)!!
        bookmarkNameEditText = alertDialog.findViewById(R.id.bookmark_name_edittext)!!
        bookmarkUrlEditText = alertDialog.findViewById(R.id.bookmark_url_edittext)!!
        folderSpinner = alertDialog.findViewById(R.id.bookmark_folder_spinner)!!
        displayOrderEditText = alertDialog.findViewById(R.id.bookmark_display_order_edittext)!!
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

        // Get the current favorite icon byte array from the cursor.
        val currentIconByteArray = bookmarkCursor.getBlob(bookmarkCursor.getColumnIndexOrThrow(FAVORITE_ICON))

        // Convert the byte array to a bitmap beginning at the first byte and ending at the last.
        val currentIconBitmap = BitmapFactory.decodeByteArray(currentIconByteArray, 0, currentIconByteArray.size)

        // Get the current bookmark values.
        val currentBookmarkName = bookmarkCursor.getString(bookmarkCursor.getColumnIndexOrThrow(BOOKMARK_NAME))
        val currentUrl = bookmarkCursor.getString(bookmarkCursor.getColumnIndexOrThrow(BOOKMARK_URL))
        val currentDisplayOrder = bookmarkCursor.getInt(bookmarkCursor.getColumnIndexOrThrow(DISPLAY_ORDER))

        // Populate the views.
        databaseIdTextView.text = bookmarkCursor.getInt(bookmarkCursor.getColumnIndexOrThrow(ID)).toString()
        currentIconImageView.setImageBitmap(currentIconBitmap)
        webpageFavoriteIconImageView.setImageBitmap(favoriteIconBitmap)
        customIconImageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.world))
        bookmarkNameEditText.setText(currentBookmarkName)
        bookmarkUrlEditText.setText(currentUrl)

        // Create an an array of column names for the matrix cursor comprised of the ID and the name.
        val matrixCursorColumnNamesArray = arrayOf(ID, BOOKMARK_NAME, PARENT_FOLDER_ID)

        // Create a matrix cursor based on the column names array.
        val matrixCursor = MatrixCursor(matrixCursorColumnNamesArray)

        // Add `Home Folder` as the first entry in the matrix folder.
        matrixCursor.addRow(arrayOf(HOME_FOLDER_DATABASE_ID, getString(R.string.home_folder), HOME_FOLDER_ID))

        // Get a cursor with the list of all the folders.
        val foldersCursor = bookmarksDatabaseHelper.getFoldersExcept(listOf())

        // Combine the matrix cursor and the folders cursor.
        val foldersMergeCursor = MergeCursor(arrayOf(matrixCursor, foldersCursor))

        // Create a resource cursor adapter for the spinner.
        val foldersCursorAdapter: ResourceCursorAdapter = object: ResourceCursorAdapter(context, R.layout.databaseview_spinner_item, foldersMergeCursor, 0) {
            override fun bindView(view: View, context: Context, cursor: Cursor) {
                // Get handles for the spinner views.
                val subfolderSpacerTextView = view.findViewById<TextView>(R.id.subfolder_spacer_textview)
                val folderIconImageView = view.findViewById<ImageView>(R.id.folder_icon_imageview)
                val folderNameTextView = view.findViewById<TextView>(R.id.folder_name_textview)

                // Populate the subfolder spacer if it is not null (the spinner is open).
                if (subfolderSpacerTextView != null) {
                    // Indent subfolders.
                    if (cursor.getLong(cursor.getColumnIndexOrThrow(PARENT_FOLDER_ID)) != HOME_FOLDER_ID) {  // The folder is not in the home folder.
                        // Get the subfolder spacer.
                        subfolderSpacerTextView.text = bookmarksDatabaseHelper.getSubfolderSpacer(cursor.getLong(cursor.getColumnIndexOrThrow(FOLDER_ID)))
                    } else {  // The folder is in the home folder.
                        // Reset the subfolder spacer.
                        subfolderSpacerTextView.text = ""
                    }
                }

                // Set the folder icon according to the type.
                if (foldersMergeCursor.position == 0) {  // The home folder.
                    // Set the gray folder image.
                   folderIconImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.folder_gray))
                } else {  // A user folder
                    // Get the folder icon byte array.
                    val folderIconByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(FAVORITE_ICON))

                    // Convert the byte array to a bitmap beginning at the first byte and ending at the last.
                    val folderIconBitmap = BitmapFactory.decodeByteArray(folderIconByteArray, 0, folderIconByteArray.size)

                    // Set the folder icon.
                    folderIconImageView.setImageBitmap(folderIconBitmap)
                }

                // Set the folder name.
                folderNameTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(BOOKMARK_NAME))
            }
        }

        // Set the folder cursor adapter drop drown view resource.
        foldersCursorAdapter.setDropDownViewResource(R.layout.databaseview_spinner_dropdown_items)

        // Set the adapter for the folder spinner.
        folderSpinner.adapter = foldersCursorAdapter

        // Get the parent folder name.
        val parentFolderId = bookmarkCursor.getLong(bookmarkCursor.getColumnIndexOrThrow(PARENT_FOLDER_ID))

        // Select the parent folder in the spinner if the bookmark isn't in the home folder.
        if (parentFolderId != HOME_FOLDER_ID) {
            // Get the database ID of the parent folder.
            val parentFolderDatabaseId = bookmarksDatabaseHelper.getFolderDatabaseId(parentFolderId)

            // Initialize the parent folder position and the iteration variable.
            var parentFolderPosition = 0
            var i = 0

            // Find the parent folder position in the folders cursor adapter.
            do {
                if (foldersCursorAdapter.getItemId(i) == parentFolderDatabaseId.toLong()) {
                    // Store the current position for the parent folder.
                    parentFolderPosition = i
                } else {
                    // Try the next entry.
                    i++
                }
                // Stop when the parent folder position is found or all the items in the folders cursor adapter have been checked.
            } while (parentFolderPosition == 0 && i < foldersCursorAdapter.count)

            // Select the parent folder in the spinner.
            folderSpinner.setSelection(parentFolderPosition)
        }

        // Store the current folder database ID.
        val currentFolderDatabaseId = folderSpinner.selectedItemId.toInt()

        // Populate the display order edit text.
        displayOrderEditText.setText(bookmarkCursor.getInt(bookmarkCursor.getColumnIndexOrThrow(DISPLAY_ORDER)).toString())

        // Initially disable the save button.
        saveButton.isEnabled = false

        // Set the radio button listeners.  These perform a click on the linear layout, which contains the necessary logic.
        currentIconRadioButton.setOnClickListener { currentIconLinearLayout.performClick() }
        webpageFavoriteIconRadioButton.setOnClickListener { webpageFavoriteIconLinearLayout.performClick() }
        customIconRadioButton.setOnClickListener { customIconLinearLayout.performClick() }

        // Set the current icon linear layout click listener.
        currentIconLinearLayout.setOnClickListener {
            // Check the current icon radio button.
            currentIconRadioButton.isChecked = true

            // Uncheck the other radio buttons.
            webpageFavoriteIconRadioButton.isChecked = false
            customIconRadioButton.isChecked = false

            // Update the save button.
            updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
        }

        // Set the webpage favorite icon linear layout click listener.
        webpageFavoriteIconLinearLayout.setOnClickListener {
            // Check the webpage favorite icon radio button.
            webpageFavoriteIconRadioButton.isChecked = true

            // Uncheck the other radio buttons.
            currentIconRadioButton.isChecked = false
            customIconRadioButton.isChecked = false

            // Update the save button.
            updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
        }

        // Set the custom icon linear layout click listener.
        customIconLinearLayout.setOnClickListener {
            // Check the custom icon radio button.
            customIconRadioButton.isChecked = true

            // Uncheck the other radio buttons.
            currentIconRadioButton.isChecked = false
            webpageFavoriteIconRadioButton.isChecked = false

            // Update the save button.
            updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
        }

        browseButton.setOnClickListener {
            // Open the file picker.
            browseActivityResultLauncher.launch("image/*")
        }

        // Update the save button if the bookmark name changes.
        bookmarkNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(editable: Editable?) {
                // Update the Save button.
                updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
            }
        })

        // Update the save button if the URL changes.
        bookmarkUrlEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(editable: Editable?) {
                // Update the save button.
                updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
            }
        })

        // Wait to set the on item selected listener until the spinner has been inflated.  Otherwise the dialog will crash on restart.
        folderSpinner.post {
            // Update the save button if the folder changes.
            folderSpinner.onItemSelectedListener = object: OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // Update the save button.
                    updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing.
                }
            }
        }

        // Update the save button if the display order changes.
        displayOrderEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(editable: Editable?) {
                // Update the save button.
                updateSaveButton(currentBookmarkName, currentUrl, currentFolderDatabaseId, currentDisplayOrder)
            }
        })

        // Allow the enter key on the keyboard to save the bookmark from the bookmark name edit text.
        bookmarkNameEditText.setOnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
            // Check the key code, event, and button status.
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && saveButton.isEnabled) {  // The enter key was pressed and the save button is enabled.
                // Trigger the listener and return the dialog fragment to the parent activity.
                editBookmarkDatabaseViewListener.saveBookmark(this, bookmarkDatabaseId)

                // Manually dismiss the alert dialog.
                alertDialog.dismiss()

                // Consume the event.
                return@setOnKeyListener true
            } else {  // If any other key was pressed, or if the save button is currently disabled, do not consume the event.
                return@setOnKeyListener false
            }
        }

        // Allow the enter key on the keyboard to save the bookmark from the URL edit text.
        bookmarkUrlEditText.setOnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
            // Check the key code, event, and button status.
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && saveButton.isEnabled) {  // The enter key was pressed and the save button is enabled.
                // Trigger the listener and return the dialog fragment to the parent activity.
                editBookmarkDatabaseViewListener.saveBookmark(this, bookmarkDatabaseId)

                // Manually dismiss the alert dialog.
                alertDialog.dismiss()

                // Consume the event.
                return@setOnKeyListener true
            } else { // If any other key was pressed, or if the save button is currently disabled, do not consume the event.
                return@setOnKeyListener false
            }
        }

        // Allow the enter key on the keyboard to save the bookmark from the display order edit text.
        displayOrderEditText.setOnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
            // Check the key code, event, and button status.
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && saveButton.isEnabled) {  // The enter key was pressed and the save button is enabled.
                // Trigger the listener and return the dialog fragment to the parent activity.
                editBookmarkDatabaseViewListener.saveBookmark(this, bookmarkDatabaseId)

                // Manually dismiss the alert dialog.
                alertDialog.dismiss()

                // Consume the event.
                return@setOnKeyListener true
            } else { // If any other key was pressed, or if the save button is currently disabled, do not consume the event.
                return@setOnKeyListener false
            }
        }

        // Return the alert dialog.
        return alertDialog
    }

    private fun updateSaveButton(currentBookmarkName: String, currentUrl: String, currentFolderDatabaseId: Int, currentDisplayOrder: Int) {
        // Get the values from the dialog.
        val newName = bookmarkNameEditText.text.toString()
        val newUrl = bookmarkUrlEditText.text.toString()
        val newFolderDatabaseId = folderSpinner.selectedItemId.toInt()
        val newDisplayOrder = displayOrderEditText.text.toString()

        // Has the favorite icon changed?
        val iconChanged = !currentIconRadioButton.isChecked

        // Has the name changed?
        val nameChanged = (newName != currentBookmarkName)

        // Has the URL changed?
        val urlChanged = (newUrl != currentUrl)

        // Has the folder changed?
        val folderChanged = (newFolderDatabaseId != currentFolderDatabaseId)

        // Has the display order changed?
        val displayOrderChanged = (newDisplayOrder != currentDisplayOrder.toString())

        // Is the display order empty?
        val displayOrderNotEmpty = newDisplayOrder.isNotEmpty()

        // Update the enabled status of the save button.
        saveButton.isEnabled = (iconChanged || nameChanged || urlChanged || folderChanged || displayOrderChanged) && displayOrderNotEmpty
    }
}
