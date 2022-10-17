package utility

import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

class ServiceAreaDestinationConfig {

    companion object {
        val config = File("wasteServiceSystemConfig.json").readText(StandardCharsets.UTF_8)
        val JsonObject = JSONObject(config)
        val LocJsonObj = JSONObject(JsonObject.get("LOCATION").toString())

        fun getXDestination(pos : String) : Int{
            return LocJsonObj.getJSONArray(pos).get(0).toString().toInt()
        }
        fun getYDestination(pos : String) : Int{
            return LocJsonObj.getJSONArray(pos).get(1).toString().toInt()
        }
    }
}