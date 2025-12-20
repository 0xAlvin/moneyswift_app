package com.example.feature.auth.impl.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature.R
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class OnboardingPage(
    val title: Int,
    val description: Int,
    val primaryCta: Int,
    val showSecondary: Boolean,
    val img: Int,
)

val onboardingPages = listOf(
    OnboardingPage(
        title = R.string.onboarding_page1_title,
        description = R.string.onboarding_page1_description,
        primaryCta = R.string.onboarding_page1_cta,
        showSecondary = false,
        img = R.drawable.shoppingbag_img
    ),
    OnboardingPage(
        title = R.string.onboarding_page2_title,
        description = R.string.onboarding_page2_description,
        primaryCta = R.string.onboarding_page2_cta,
        showSecondary = false,
        img = R.drawable.bag_img
    ),
    OnboardingPage(
        title = R.string.onboarding_page3_title,
        description = R.string.onboarding_page3_description,
        primaryCta = R.string.onboarding_page3_cta,
        showSecondary = true,
        img = R.drawable.cart
    )
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    onSignIn: () -> Unit,
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == true) {
            onAuthenticated()
        }
    }

    if (isAuthenticated == false) {
        val pagerState = rememberPagerState { onboardingPages.size }
        val scope = rememberCoroutineScope()

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    if (pagerState.currentPage < onboardingPages.lastIndex) {
                        TextButton(
                            onClick = { onFinish() },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Text(
                                "Skip",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    OnboardingPageContent(page, pagerState)
                }

                Spacer(Modifier.height(24.dp))

                PagerIndicators(pagerState)

                Spacer(Modifier.height(32.dp))

                BottomActions(
                    pagerState = pagerState,
                    onPrimary = {
                        if (pagerState.currentPage == onboardingPages.lastIndex) {
                            onFinish()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    onSecondary = onSignIn
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    index: Int,
    pagerState: PagerState
) {
    val page = onboardingPages[index]
    val pageOffset =
        ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue

    val scale by animate_toggle_scale(pageOffset)
    val alpha by animate_toggle_alpha(pageOffset)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .scale(scale)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Box {
                Image(
                    painter = painterResource(page.img),
                    contentDescription = stringResource(page.title),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f)
                                ),
                                startY = 300f
                            )
                        )
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun PagerIndicators(pagerState: PagerState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            val selected = pagerState.currentPage == index
            val width by animateDpAsState(
                targetValue = if (selected) 32.dp else 8.dp,
                label = "indicator_width",
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )

            val color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun BottomActions(
    pagerState: PagerState,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit
) {
    val isLast = pagerState.currentPage == onboardingPages.lastIndex

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Button(
            onClick = onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Text(
                text = stringResource(onboardingPages[pagerState.currentPage].primaryCta),
                style = MaterialTheme.typography.labelLarge
            )
        }

        AnimatedVisibility(
            visible = isLast,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                animationSpec = tween(300),
                initialOffsetY = { it / 2 }
            ),
            exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                animationSpec = tween(200),
                targetOffsetY = { it / 2 }
            )
        ) {
            TextButton(
                onClick = onSecondary,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    "Already have an account? Sign in",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun animate_toggle_scale(pageOffset: Float): State<Float> =
    animateFloatAsState(
        targetValue = 1f - (pageOffset * 0.08f).coerceIn(0f, 0.12f),
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "scale"
    )

@Composable
private fun animate_toggle_alpha(pageOffset: Float): State<Float> =
    animateFloatAsState(
        targetValue = 1f - (pageOffset * 0.4f).coerceIn(0f, 1f),
        animationSpec = tween(400, easing = LinearOutSlowInEasing),
        label = "alpha"
    )