package com.example.feature.cart.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.core.domain.model.CartItem
import com.example.feature.databinding.ItemCartBinding
import kotlinx.serialization.InternalSerializationApi

class CartAdapter(
    private val onRemove: (Int) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @OptIn(InternalSerializationApi::class)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            productTitle.text = item.product.title
            productPrice.text = "$${String.format("%.2f", item.totalPrice)}"
            quantityText.text = "Qty: ${item.quantity}"

            productImage.load(item.product.image) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
            }

            deleteBtn.setOnClickListener { onRemove(item.product.id) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        @OptIn(InternalSerializationApi::class)
        override fun areItemsTheSame(old: CartItem, new: CartItem) = old.product.id == new.product.id
        override fun areContentsTheSame(old: CartItem, new: CartItem) = old == new
    }
}