package com.example.mercadogil.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object TextWatcherUtils {
    fun configureTextWatcher(editText: EditText, updateTotalPrice: () -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            private var originalText: String? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                originalText = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString()

                if (currentText.isNotEmpty() && currentText != originalText) {
                    editText.removeTextChangedListener(this)

                    val formattedText = CurrencyUtils.formatValueAsCurrency(CurrencyUtils.convertToCurrency(currentText))

                    editText.setText(formattedText)
                    editText.setSelection(formattedText.length)
                    editText.addTextChangedListener(this)
                }

                updateTotalPrice()
            }
        })
    }
}