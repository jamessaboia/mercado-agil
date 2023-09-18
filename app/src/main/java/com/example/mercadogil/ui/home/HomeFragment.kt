package com.example.mercadogil.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mercadogil.R
import com.example.mercadogil.data.entity.ProductEntity
import com.example.mercadogil.databinding.FragmentHomeBinding
import com.example.mercadogil.ui.addproduct.ProductAdapter
import com.example.mercadogil.ui.main.MainActivity
import com.example.mercadogil.ui.main.ProductViewModel
import com.example.mercadogil.utils.CurrencyUtils.formatValueAsCurrency
import com.example.mercadogil.utils.FragmentConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        val TAG = HomeFragment::class.simpleName
        private const val LOADING_TIME = 500L
        private const val INTENT_TYPE_TEXT = "text/plain"
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private var productList: List<ProductEntity>? = null
    private var shareProductListIntent: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmer()
        productViewModel.loadData()
        shareProductListIntent = Intent(Intent.ACTION_SEND)
        setupRecyclerView()
        observeProductList()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setupLayoutNavigation(FragmentConfig.HOME)
        setupClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        shimmerLayout.stopShimmer()
    }

    private fun startShimmer() {
        binding.run {
            shimmerLayout = skeletonLoading.shimmerViewContainer
            shimmerLayout.startShimmer()
        }
    }

    private fun observeProductList() {
        productViewModel.getAllProducts().observe(viewLifecycleOwner) { productList ->
            this.productList = productList
            productAdapter.submitList(productList)
            handleEmptyListState()
            updateTotalPriceValue()

            this.productList?.let {
                shareProductListIntent?.putExtra(Intent.EXTRA_TEXT, createSharedListMessage())
            }
        }
    }

    private fun setupRecyclerView() {
        binding.run {
            productAdapter = ProductAdapter(requireContext(), productViewModel)
            recyclerView.itemAnimator = null
            recyclerView.adapter = productAdapter
        }
    }

    private fun setupClickListener() {
        binding.run {
            btnNewProduct.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_addProduct)
            }

            val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
            toolbar?.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_share_list -> {
                        if (productList?.isNotEmpty() == true) {
                            shareProductList()
                        } else {
                            showEmptyListMessage()
                        }
                        true
                    }
                    R.id.menu_clear_all_products -> {
                        shareProductListIntent?.removeExtra(Intent.EXTRA_TEXT)
                        productViewModel.deleteAllProducts()
                        if (productViewModel.isProductListEmpty()) {
                            showEmptyListMessage()
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun handleEmptyListState() {
        binding.run {
            if (productViewModel.isProductListEmpty()) {
                shareProductListIntent?.removeExtra(Intent.EXTRA_TEXT)
                shimmerLayout.visibility = GONE
                emptyListMessage.visibility = VISIBLE
            } else {
                emptyListMessage.visibility = GONE
                showLoading()
            }
        }
    }

    private fun updateTotalPriceValue() {
        val totalValue = productViewModel.getTotalPrice()
        val totalPriceText = getString(R.string.total_price_label)
        binding.tvTotalPrice.text = String.format(totalPriceText, formatValueAsCurrency(totalValue))
    }

    private fun showLoading() {
        binding.run {
            recyclerView.visibility = GONE
            shimmerLayout.visibility = VISIBLE

            shimmerLayout.postDelayed({
                recyclerView.visibility = VISIBLE
                shimmerLayout.visibility = GONE
            }, LOADING_TIME)
        }
    }

    private fun shareProductList() {
        shareProductListIntent?.apply {
            type = INTENT_TYPE_TEXT
            if (hasExtra(Intent.EXTRA_TEXT)) {
                startActivity(Intent.createChooser(this, getString(R.string.intent_title_shared)))
            }
        }
    }

    private fun createSharedListMessage(): String {
        val builder = StringBuilder()
        builder.apply {
            append(getString(R.string.shared_message_title))
            productList?.let { products ->
                for (product in products) {
                    append(getString(R.string.shared_message_product_name, product.name))
                    append(getString(R.string.shared_message_product_amount, product.amount.toString()))
                    append(getString(R.string.shared_message_product_price, formatValueAsCurrency(product.price)))
                }
            }
            append(getString(R.string.shared_list_message_total_value, formatValueAsCurrency(productViewModel.getTotalPrice())))
            append(getString(R.string.shared_list_message_footer))
            return this.toString()
        }
    }

    private fun showEmptyListMessage() {
        Snackbar.make(binding.root, getString(R.string.empty_list_snackkbar_message), Snackbar.LENGTH_SHORT)
            .show()
    }
}