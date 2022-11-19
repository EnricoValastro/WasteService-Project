package utility

import it.unibo.radarSystem22.domain.interfaces.ILed
import it.unibo.radarSystem22.domain.models.LedModel
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

class LedFactory {

    companion object{

        private fun configure(){
            var config = File("DomainSystemConfig.json").readText(StandardCharsets.UTF_8)
            var jsonObj = JSONObject(config)
            DomainSystemConfig.simulation = jsonObj.getBoolean("SIMULATION")
            DomainSystemConfig.ledGui = jsonObj.getBoolean("LEDGUI")
        }

        fun createLed() : ILed {
            configure()
            return LedModel.create()
        }

    }


}