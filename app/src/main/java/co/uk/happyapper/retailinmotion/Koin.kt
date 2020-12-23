package co.uk.happyapper.retailinmotion
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object Koin {
    val main: Module = module {
        single { LuasRepo(LuasApiService.create())}
        single<ClockService> { RetailInMotionClockService() }
    }

    val viewModels: Module = module {
        viewModel { MainViewModel(get(), get()) }
    }
}
