package co.uk.happyapper.retailinmotion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import co.uk.happyapper.retailinmotion.repo.Resource
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {
    val luasRepo = mockk<LuasRepo>()
    val clockService = mockk<ClockService>()
    private val observer: Observer<LuasUI?> = mockk()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner

    @Before
    fun setUp() {
        lifeCycleTestOwner = LifeCycleTestOwner()
        lifeCycleTestOwner.onCreate()
        val outtram = TramDestination("TestOut", "0")
        val intram = TramDestination("TestIn", "0")
        val outbound = LuasDirection("Outbound", listOf(outtram))
        val inbound = LuasDirection("Inbound", listOf(intram))
        val stillorgan = StopInfo("STILLORGAN", "STI", "", listOf(outbound, inbound))
        val marlborough = StopInfo("MARLBOROUGH", "MAR", "", listOf(outbound, inbound))
        every { clockService.getNow() } returns 1608831974713
        every { luasRepo.getStopInfo("STI") } answers { MutableLiveData(Resource.success(stillorgan)) }
        every { luasRepo.getStopInfo("MAR") } answers { MutableLiveData(Resource.success(marlborough)) }
    }

    @Test
    fun testInboundTramsWhenPM() {
        every { clockService.getTimeFromMillis(1608831974713) } returns 1746
        lifeCycleTestOwner.onResume()
        val viewModel: MainViewModel = MainViewModel(luasRepo, clockService)
        every { observer.onChanged(any()) } answers {}
        viewModel.data.observeForever(observer)
        viewModel.refresh()
        val test = (viewModel.data.value as LuasUI.LuasData).data.destinations?.get(0)?.destination
        assert(test.equals("TestIn"))
        val station = (viewModel.data.value as LuasUI.LuasData).data.station
        assert(station.equals("STILLORGAN"))
    }

    @Test
    fun testOutboundTramsWhenAM() {
        every { clockService.getTimeFromMillis(1608831974713) } returns 1115
        lifeCycleTestOwner.onResume()
        val viewModel: MainViewModel = MainViewModel(luasRepo, clockService)
        every { observer.onChanged(any()) } answers {}
        viewModel.data.observeForever(observer)
        viewModel.refresh()
        val test = (viewModel.data.value as LuasUI.LuasData).data.destinations?.get(0)?.destination
        assert(test.equals("TestOut"))
        val station = (viewModel.data.value as LuasUI.LuasData).data.station
        assert(station.equals("MARLBOROUGH"))
    }
}

class LifeCycleTestOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return registry
    }

    fun onCreate() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onResume() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onDestroy() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}