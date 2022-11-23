package utility

import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

class AdvancedTestingModeSetUp {

    companion object{
        fun checkForTestingMode() : Boolean{
            var config = File("DomainSystemConfig.json").readText(StandardCharsets.UTF_8)
            var jsonObj = JSONObject(config)
            return jsonObj.getBoolean("ADVANCEDTESTINGMODE")
        }
    }
}