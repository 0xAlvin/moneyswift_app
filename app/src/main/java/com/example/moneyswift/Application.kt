package com.example.moneyswift

import android.app.Application
import com.example.moneyswift.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import com.stripe.android.PaymentConfiguration

@HiltAndroidApp
class MoneySwiftApp : Application(){
    override fun onCreate() {
        super.onCreate()

        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.STRIPE_PUBLISHABLE_KEY
        )
    }
}
