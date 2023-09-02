package com.example.mercadogil.ui.addproduct

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadogil.R
import com.example.mercadogil.data.entity.ProductEntity
import com.example.mercadogil.databinding.ProductItemBinding
import com.example.mercadogil.extensions.tryToLoadImage
import com.example.mercadogil.ui.home.HomeFragmentDirections
import com.example.mercadogil.ui.main.ProductViewModel
import com.example.mercadogil.utils.CurrencyUtils.formatValueAsCurrency
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ProductAdapter @Inject constructor(
    private val context: Context,
    private val productViewModel: ProductViewModel
) : ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    private lateinit var binding: ProductItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var product: ProductEntity

        init {
            binding.btnDeleteProduct.setOnClickListener {
                showDeleteDialog()
            }
        }

        fun bind(product: ProductEntity) {
            this.product = product
            binding.run {
                imgProductImage.tryToLoadImage(product.image)
                tvProductName.text = product.name
                tvProductPrice.text =
                    context.getString(R.string.product_item_price, formatValueAsCurrency(product.price))
                tvProductAmount.text = itemView.context.getString(R.string.product_item_amount, product.amount)

                itemView.rootView.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeToEditProduct(product)
                    Navigation.findNavController(it).navigate(action)
                }
            }
        }

        private fun showDeleteDialog() {
            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.delete_dialog_title))
                .setMessage(context.getString(R.string.delete_dialog_subtitle))
                .setNeutralButton(context.getString(R.string.delete_dialog_btn_cancel)) { _, _ -> }
                .setPositiveButton(context.getString(R.string.delete_dialog_btn_delete)) { _, _ ->
                    productViewModel.deleteProduct(product)
                }
                .show()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
