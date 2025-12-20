package com.example.feature.cart.impl

import android.view.ContextThemeWrapper
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feature.R
import com.example.feature.databinding.FragmentCartBinding

@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onCheckout: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val items by viewModel.cartItems.collectAsStateWithLifecycle()
    val subtotal by viewModel.subtotal.collectAsStateWithLifecycle()
    val shipping by viewModel.shipping.collectAsStateWithLifecycle()
    val tax by viewModel.tax.collectAsStateWithLifecycle()
    val total by viewModel.totalAmount.collectAsStateWithLifecycle()


    AndroidViewBinding(
        factory = { inflater, parent, attachToParent ->
            val themedContext = ContextThemeWrapper(
                inflater.context,
                R.style.Theme_MoneySwift_XmlViews
            )
            val themedInflater = inflater.cloneInContext(themedContext)
            FragmentCartBinding.inflate(themedInflater, parent, attachToParent)
        }
    ) {
        backButton.setOnClickListener { onBackClick() }

        if (cartRecyclerView.layoutManager == null) {
            cartRecyclerView.layoutManager = LinearLayoutManager(root.context)
        }

        val adapter = cartRecyclerView.adapter as? CartAdapter ?: CartAdapter { id ->
            viewModel.removeItem(id)
        }.also {
            cartRecyclerView.adapter = it
        }

        if (items.isEmpty()) {
            emptyCartText.visibility = View.VISIBLE
            cartContentLayout.visibility = View.GONE
        } else {
            emptyCartText.visibility = View.GONE
            cartContentLayout.visibility = View.VISIBLE

            adapter.submitList(items)

            subtotalRow.label.text =
                subtotalRow.label.context.getString(R.string.subtotal_text_cart)

            subtotalRow.value.text =
                subtotalRow.value.context.getString(R.string.price_format, subtotal)

            shippingRow.label.text =
                shippingRow.label.context.getString(R.string.shipping_text_cart)
            shippingRow.value.text =
                shippingRow.value.context.getString(R.string.price_format, shipping)

            taxRow.label.text =
                taxRow.label.context.getString(R.string.tax_8_text_cart)
            taxRow.value.text =
                taxRow.value.context.getString(R.string.price_format, tax)

            totalTextView.text =
                totalTextView.context.getString(R.string.price_format, total)

        }

        checkoutButton.isEnabled = items.isNotEmpty()
        checkoutButton.alpha = if (items.isNotEmpty()) 1f else 0.5f
        checkoutButton.setOnClickListener {
            if (items.isNotEmpty()) {
                onCheckout()
            }
        }
    }
}