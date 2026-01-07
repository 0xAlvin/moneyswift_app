package com.example.feature.checkout.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.repository.CartRepositoryInterface
import com.example.core.domain.repository.PaymentRepositoryInterface
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

sealed class CheckoutState {
    object Idle : CheckoutState()
    object LoadingPaymentSheet : CheckoutState()
    data class ReadyForPayment(
        val clientSecret: String,
        val configuration: PaymentSheet.Configuration
    ) : CheckoutState()
    object Processing : CheckoutState()
    data class Success(val orderNumber: String, val amount: Double, val date: String) : CheckoutState()
    data class Failed(val message: String) : CheckoutState()
}

data class ProcessingStep(val text: String, val isComplete: Boolean, val isActive: Boolean)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepositoryInterface,
    val paymentRepository: PaymentRepositoryInterface
) : ViewModel() {

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    private val _processingSteps = MutableStateFlow<List<ProcessingStep>>(emptyList())
    val processingSteps: StateFlow<List<ProcessingStep>> = _processingSteps.asStateFlow()

    @OptIn(InternalSerializationApi::class)
    val cartTotal = cartRepository.getCartItems()
        .map { items ->
            val sub = items.sumOf { it.totalPrice }
            val ship = if (sub > 0) 10.0 else 0.0
            val taxAmount = sub * 0.08
            sub + ship + taxAmount }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private var currentPaymentAmount: Double = 0.0

    fun initializePaymentSheet(totalAmount: Double) {
        if (totalAmount <= 0.0) return

        viewModelScope.launch {
            _checkoutState.value = CheckoutState.LoadingPaymentSheet
            currentPaymentAmount = totalAmount

            val result = paymentRepository.createPaymentIntent(totalAmount)

            result.onSuccess { paymentConfig ->
                val configuration = PaymentSheet.Configuration.Builder("MoneySwift")
                    .apply {
                        paymentConfig.customerConfig?.let { customer(it) }
                        googlePay(
                            PaymentSheet.GooglePayConfiguration(
                                environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
                                countryCode = "US",
                                currencyCode = "USD"
                            )
                        )
                    }
                    .build()

                _checkoutState.value = CheckoutState.ReadyForPayment(
                    clientSecret = paymentConfig.clientSecret,
                    configuration = configuration
                )
            }.onFailure { error ->
                _checkoutState.value = CheckoutState.Failed(
                    error.message ?: "Failed to initialize payment."
                )
            }
        }
    }

    fun handlePaymentSheetResult(result: PaymentSheetResult, amount: Double) {
        when (result) {
            is PaymentSheetResult.Completed -> {
                viewModelScope.launch {
                    _checkoutState.value = CheckoutState.Processing
                    _processingSteps.value = listOf(
                        ProcessingStep("Confirming payment", isComplete = false, isActive = true),
                        ProcessingStep("Processing order", isComplete = false, isActive = false),
                        ProcessingStep("Finalizing transaction",
                            isComplete = false,
                            isActive = false
                        )
                    )

                    updateStep(0, true)
                    updateStep(1, true)
                    updateStep(2, true)

                    clearCart()

                    _checkoutState.value = CheckoutState.Success(
                        orderNumber = generateOrderNumber(),
                        amount = amount,
                        date = getCurrentDate()
                    )
                }
            }
            is PaymentSheetResult.Canceled -> {
                _checkoutState.value = CheckoutState.Idle
            }
            is PaymentSheetResult.Failed -> {
                _checkoutState.value = CheckoutState.Failed(
                    result.error.message ?: "Payment failed. Please try again."
                )
            }
        }
    }

    private fun updateStep(index: Int, isActive: Boolean) {
        _processingSteps.value = _processingSteps.value.mapIndexed { i, step ->
            when {
                i < index -> step.copy(isComplete = true, isActive = false)
                i == index -> step.copy(isComplete = !isActive, isActive = isActive)
                else -> step
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    private suspend fun clearCart() {
        cartRepository.getCartItems().firstOrNull()?.forEach { cartItem ->
            cartRepository.removeFromCart(cartItem.product.id)
        }
    }

    private fun generateOrderNumber() = "MS-${(1000..9999).random()}-${(1000..9999).random()}"
    private fun getCurrentDate() = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
}