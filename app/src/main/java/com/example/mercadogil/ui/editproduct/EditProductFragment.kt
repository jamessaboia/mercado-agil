package com.example.mercadogil.ui.editproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mercadogil.R
import com.example.mercadogil.data.entity.ProductEntity
import com.example.mercadogil.databinding.FragmentEditProductBinding
import com.example.mercadogil.extensions.tryToLoadImage
import com.example.mercadogil.ui.dialogs.AddImageDialog
import com.example.mercadogil.ui.main.MainActivity
import com.example.mercadogil.ui.main.ProductViewModel
import com.example.mercadogil.utils.Constants.DEFAULT_PRODUCT_AMOUNT
import com.example.mercadogil.utils.Constants.DEFAULT_PRODUCT_PRICE
import com.example.mercadogil.utils.CurrencyUtils.convertToCurrency
import com.example.mercadogil.utils.CurrencyUtils.formatValueAsCurrency
import com.example.mercadogil.utils.FragmentConfig
import com.example.mercadogil.utils.TextWatcherUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    companion object {
        val TAG = EditProductFragment::class.simpleName
    }

    private val productData by navArgs<EditProductFragmentArgs>()
    private lateinit var binding: FragmentEditProductBinding
    private var currentProductAmount = DEFAULT_PRODUCT_AMOUNT
    private var productPrice = DEFAULT_PRODUCT_PRICE
    private var productImageUrl: String? = null
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateProductData()
        updateTotalPriceValue()
        setupBinding()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setupLayoutNavigation(FragmentConfig.EDIT_PRODUCT)
    }

    private fun setupBinding() {
        binding.run {
            TextWatcherUtils.configureTextWatcher(binding.itProductPrice) {
                updateTotalPriceValue()
            }

            productImageContainer.setOnClickListener {
                AddImageDialog(requireContext()).show(productData.data.image) { imageUrl ->
                    productImageUrl = imageUrl
                    addImage.tryToLoadImage(productImageUrl)
                }
            }
            btnIncreaseProductAmount.setOnClickListener {
                increaseProductAmount()
            }
            btnDecreaseProductAmount.setOnClickListener {
                decreaseProductAmount()
            }
            btnEditProduct.setOnClickListener {
                validateAndEditProduct()
            }
        }
    }

    private fun populateProductData() {
        binding.run {
            productImageUrl = productData.data.image
            if (!productImageUrl.isNullOrEmpty()) {
                addImage.tryToLoadImage(productImageUrl)
            } else {
                addImage.setImageResource(R.drawable.default_image)
            }

            itProductName.setText(productData.data.name)
            itProductPrice.setText(formatValueAsCurrency(productData.data.price))
            tvProductAmountCount.text = productData.data.amount.toString()
            currentProductAmount = productData.data.amount
            productPrice = productData.data.price
        }
    }

    private fun increaseProductAmount() {
        currentProductAmount++
        updateProductAmountTextView()
        updateTotalPriceValue()
    }

    private fun decreaseProductAmount() {
        if (currentProductAmount > 1) {
            currentProductAmount--
            updateProductAmountTextView()
            updateTotalPriceValue()
        }
    }

    private fun validateAndEditProduct() {
        binding.run {
            val productName = binding.itProductName.text.toString()

            if (productName.isNotEmpty()) {
                editProduct()
                findNavController().popBackStack()
            } else {
                showEmptyNameMessage()
            }
        }
    }

    private fun updateProductAmountTextView() {
        binding.tvProductAmountCount.text = currentProductAmount.toString()
    }

    private fun updateTotalPriceValue() {
        productPrice = convertToCurrency(binding.itProductPrice.text.toString())
        val totalValue = productPrice * currentProductAmount
        val totalPriceText = getString(R.string.total_price_label)
        binding.tvTotalPrice.text = String.format(totalPriceText, formatValueAsCurrency(totalValue))
    }

    private fun editProduct() {
        val editedProduct = ProductEntity(
            id = productData.data.id,
            name = binding.itProductName.text.toString(),
            price = productPrice,
            amount = currentProductAmount,
            image = productImageUrl
        )
        productViewModel.insertProduct(editedProduct)
    }

    private fun showEmptyNameMessage() {
        Snackbar.make(
            requireView(),
            getString(R.string.empty_name_snackkbar_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}