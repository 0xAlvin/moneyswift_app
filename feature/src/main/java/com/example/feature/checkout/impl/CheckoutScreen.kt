package com.example.feature.checkout.impl

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheet.Builder
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val checkoutState by viewModel.checkoutState.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()
    val steps by viewModel.processingSteps.collectAsState()

    val paymentResultCallback = { result:PaymentSheetResult ->
        viewModel.handlePaymentSheetResult(result, cartTotal)
    }
    val paymentSheet = remember(paymentResultCallback) { Builder(paymentResultCallback) }.build()

    LaunchedEffect(cartTotal) {
        if (checkoutState is CheckoutState.Idle && cartTotal > 0.0) {
            viewModel.initializePaymentSheet(cartTotal)
        }
    }

    LaunchedEffect(checkoutState) {
        if (checkoutState is CheckoutState.ReadyForPayment) {
            val state = checkoutState as CheckoutState.ReadyForPayment
            paymentSheet.presentWithPaymentIntent(state.clientSecret, state.configuration)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (checkoutState) {
                            is CheckoutState.Success -> "Order Placed"
                            is CheckoutState.Failed -> "Payment Error"
                            else -> "Checkout"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    if (checkoutState !is CheckoutState.Success && checkoutState !is CheckoutState.Processing) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(
                targetState = checkoutState,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "state_transition"
            ) { state ->
                when (state) {
                    is CheckoutState.Idle -> IdleView(cartTotal) {
                        viewModel.initializePaymentSheet(cartTotal)
                    }
                    is CheckoutState.LoadingPaymentSheet, is CheckoutState.ReadyForPayment -> LoadingView()
                    is CheckoutState.Processing -> ProcessingView(steps)
                    is CheckoutState.Success -> SuccessView(state, onContinueShopping)
                    is CheckoutState.Failed -> FailedView(state.message, {
                        viewModel.initializePaymentSheet(cartTotal)
                    }, onNavigateBack)
                }
            }
        }
    }
}

@Composable
fun IdleView(amount: Double, onPay: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Total Amount Due", style = MaterialTheme.typography.bodyLarge)
        Text("$${String.format("%.2f", amount)}", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onPay,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Proceed to Payment", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
            Spacer(Modifier.height(16.dp))
            Text("Securing Connection...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ProcessingView(steps: List<ProcessingStep>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)), label = ""
        )
        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(64.dp).rotate(rotation), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))
        Text("Processing Payment", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(48.dp))
        ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                steps.forEach { step ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (step.isComplete) Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                        else if (step.isActive) CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                        else Icon(Icons.Default.RadioButtonUnchecked, null, tint = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.width(16.dp))
                        Text(step.text, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessView(state: CheckoutState.Success, onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(Modifier.size(100.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, null, Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Payment Received!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))
        OutlinedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailRow("Order Number", state.orderNumber)
                DetailRow("Date", state.date)
                HorizontalDivider()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Paid", fontWeight = FontWeight.Bold)
                    Text("$${String.format("%.2f", state.amount)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(Modifier.height(40.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().height(56.dp), shape = MaterialTheme.shapes.large) {
            Text("Continue Shopping")
        }
    }
}

@Composable
fun FailedView(message: String, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Error, null, Modifier.size(80.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(24.dp))
        Text("Transaction Failed", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(message, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(40.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
            Text("Try Again")
        }
        TextButton(onClick = onBack) { Text("Go Back") }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
