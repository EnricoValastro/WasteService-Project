package utility

import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

object SonarFilterConfig {

    fun configSonarFromFile() : Int {
        val config = File("wasteServiceSystemConfig.json").readText(StandardCharsets.UTF_8)
        val JsonObject = JSONObject(config)
        return JsonObject.getInt("DLIMIT")
    }

}