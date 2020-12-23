package co.uk.happyapper.retailinmotion

import android.app.Application

import co.uk.happyapper.retailinmotion.Koin.main
import co.uk.happyapper.retailinmotion.Koin.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RetailInMotionApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    main,
                    viewModels
                )
            )
        }
    }
}
