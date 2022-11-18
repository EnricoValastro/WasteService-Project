package wasteservice.state

import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

enum class Material {
    PLASTIC, GLASS
}

data class ContainerState(
        private val boxMaxWeight : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap(),
        private val boxCurrentWeight : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap()
){
    /* Config boxMaxWeigth from JSON file */
    init {
        val config = File("wasteServiceSystemConfig.json").readText(StandardCharsets.UTF_8)
        val JsonObject = JSONObject(config)
        val maxPb = JsonObject.getDouble("MAXPB")
        val maxGb = JsonObject.getDouble("MAXGB")
        boxMaxWeight[Material.PLASTIC] = boxMaxWeight[Material.PLASTIC]!!.plus(maxPb)
        boxMaxWeight[Material.GLASS]   = boxMaxWeight[Material.GLASS]!!.plus(maxGb)
    }

    fun updateBoxWeight(material: Material, value : Double){
        boxCurrentWeight[material] = boxCurrentWeight[material]!!.plus(value)
    }

    fun canStore(material: Material, value: Double) : Boolean {
        return (boxCurrentWeight[material]!!+value <= boxMaxWeight[material]!!)
    }

}
