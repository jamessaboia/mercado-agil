package com.example.mercadogil.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.example.mercadogil.R
import com.example.mercadogil.databinding.AddImageDialogBinding
import com.example.mercadogil.extensions.tryToLoadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddImageDialog(private val context: Context) {
    private val binding = AddImageDialogBinding.inflate(LayoutInflater.from(context))

    fun show(defaultUrl: String? = null,
             whenImageIsLoad: (imageUrl: String) -> Unit
    ) {
        binding.run {

            defaultUrl?.let {
                ivProductImage.tryToLoadImage(it)
                itProductImageUrl.setText(it)
            }

            btnLoadImage.setOnClickListener {
                val productImageUrl = itProductImageUrl.text.toString()
                ivProductImage.tryToLoadImage(productImageUrl)
            }

            MaterialAlertDialogBuilder(context)
                .setView(binding.root)
                .setNeutralButton(context.getString(R.string.delete_dialog_btn_cancel)) { _, _ -> }
                .setPositiveButton(context.getString(R.string.add_image_confirm_btn)) { _, _ ->
                   val  productImageUrl = itProductImageUrl.text.toString()
                    whenImageIsLoad(productImageUrl)
                }
                .show()
        }
    }
}