package wasteservice.state

import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

enum class Material {
    PLASTIC, GLASS
}
data class ServiceAreaState(
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

    /* Use Gson library to retrive a ServiceAreaState obj from its JSON String representation */
    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : ServiceAreaState{
            return gson.fromJson(str, ServiceAreaState::class.java)
        }
    }

    fun updateBoxWeight(material: Material, value : Double){
        boxCurrentWeight[material] = boxCurrentWeight[material]!!.plus(value)
    }

    fun getCurrentBoxWeight(material: Material) : Double{
        return boxCurrentWeight[material]!!
    }
    fun getMaxBoxWeight(material: Material) : Double{
        return boxMaxWeight[material]!!
    }

    fun canStore(material: Material, value: Double) : Boolean {
        return (boxCurrentWeight[material]!!+value <= boxMaxWeight[material]!!)
    }

    /* Use Gson library to create a JSON String representation of a ServiceAreaState obj */
    fun toJsonString() : String{
        return ServiceAreaState.gson.toJson(this)
    }
}
