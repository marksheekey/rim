package co.uk.happyapper.retailinmotion
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


class LuasRepo(private val apiService: LuasApiService) {
    fun getStopInfo(stop: String) = NetworkCall<StopInfo>().makeCall(apiService.getStopInfo(stop))
}


@Root(name = "stopInfo", strict = false)
class StopInfo @JvmOverloads constructor(
    @field: Attribute(name = "stop", required = false)
    var stopName: String = "",
    @field: Attribute(name = "stopAbv", required = false)
    var stop: String = "",
    @field: Element(name = "message", required = false)
    var message: String? = null,
    @field: ElementList(name = "direction", required = false, inline = true)
    var directions: List<LuasDirection>? = null
)

@Root(name = "direction", strict = false)
data class LuasDirection @JvmOverloads constructor(
    @field: Attribute(name = "name", required = false)
    var name: String? = null,
    @field: ElementList(name = "tram", required = false, inline = true)
    var tramDestinations: List<TramDestination>? = null
)

@Root(name = "tram", strict = false)
class TramDestination @JvmOverloads constructor(
    @field: Attribute(name = "destination",required = false)
    var destination: String? = null,
    @field: Attribute(name = "dueMins", required = false)
    var dueMins: String? = null
)