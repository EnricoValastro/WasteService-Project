package wasteservice.state

import com.google.gson.Gson
import org.json.JSONObject

enum class Material {
    PLASTIC, GLASS
}

data class WasteServiceState(private val boxMaxWeight : MutableMap<Material, Double>,
        private val boxCurrentWeight : MutableMap<Material, Double> =
                Material.values().associateWith { 0.0 }.toMutableMap()
) {

    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : WasteServiceState{
            return gson.fromJson(str, WasteServiceState::class.java)
        }
    }

    fun updateBoxWeight(material: Material, value : Double){
        boxCurrentWeight[material] = boxCurrentWeight[material]!!.plus(value)
    }

    fun getCurrentBoxWeight(material: Material) : Double{
        return boxCurrentWeight[material]!!
    }

    fun canStore(material: Material, value: Double) : Boolean {
        return (boxCurrentWeight[material]!!+value <= boxMaxWeight[material]!!)
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }
}

fun String.toWasteServiceState() : WasteServiceState{
    return WasteServiceState.fromJsonString(this)
}
