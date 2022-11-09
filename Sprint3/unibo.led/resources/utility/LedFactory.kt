package utility

import it.unibo.radarSystem22.domain.interfaces.ILed
import it.unibo.radarSystem22.domain.models.LedModel
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

class LedFactory {

    companion object{
        private var simulation : Boolean = true
        private var ledGui : Boolean = true


        private fun configure(){
            var config = File("DomainSystemConfig.json").readText(StandardCharsets.UTF_8)
            var jsonObj = JSONObject(config)
            simulation = jsonObj.getBoolean("SIMULATION")
            ledGui = jsonObj.getBoolean("LEDGUI")
            DomainSystemConfig.ledGui = ledGui
            DomainSystemConfig.simulation = simulation
        }

        fun createLed() : ILed {
            configure()
            return LedModel.create()
        }

    }


}