package sys.state

import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

enum class Material {
    PLASTIC, GLASS
}
enum class CurrStateTrolley {
    IDLE, STOPPED, MOVING, PICKINGUP, DROPPINGOUT
}
enum class TTPosition{
    HOME, INDOOR, PLASTICBOX, GLASSBOX, ONTHEROAD
}
enum class CurrStateLed {
    ON, OFF, BLINKING
}

data class SystemState (
        private var currLedState : CurrStateLed = CurrStateLed.OFF,
        private var currState : CurrStateTrolley = CurrStateTrolley.IDLE,
        private var currPosition : TTPosition = TTPosition.HOME,
        private val boxMaxWeight : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap(),
        private val boxCurrentWeight : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap()
        ) {

    /* Config boxMaxWeigth from JSON file */
    init {
        val config = File("wasteServiceSystemConfig.json").readText(StandardCharsets.UTF_8)
        val JsonObject = JSONObject(config)
        val maxPb = JsonObject.getDouble("MAXPB")
        val maxGb = JsonObject.getDouble("MAXGB")
        boxMaxWeight[Material.PLASTIC] = boxMaxWeight[Material.PLASTIC]!!.plus(maxPb)
        boxMaxWeight[Material.GLASS]   = boxMaxWeight[Material.GLASS]!!.plus(maxGb)
    }

    /* Use Gson library to deserialize */
    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : SystemState {
            return gson.fromJson(str, SystemState::class.java)
        }
    }

    /* Use Gson library to serialize obj */
    fun toJsonString() : String{
        return gson.toJson(this)
    }

    /* For container state managing */
    fun updateBoxWeight(material: Material, value : Double){
        boxCurrentWeight[material] = boxCurrentWeight[material]!!.plus(value)
    }
    fun getCurrentBoxWeight(material: Material) : Double{
        return boxCurrentWeight[material]!!
    }
    fun getAllCurrentBoxWeight() : String {
        return boxCurrentWeight.toString()
    }
    fun getMaxBoxWeight(material: Material) : Double{
        return boxMaxWeight[material]!!
    }
    fun canStore(material: Material, value: Double) : Boolean {
        return (boxCurrentWeight[material]!!+value <= boxMaxWeight[material]!!)
    }

    /* For transportTrolley state/position managing */
    fun setCurrState(state : CurrStateTrolley){
        currState = state
    }
    fun setCurrPosition(position : TTPosition){
        currPosition = position
    }
    fun getCurrState() : CurrStateTrolley {
        return currState
    }
    fun getCurrPosition() : TTPosition {
        return currPosition
    }

    /* For led state managing */
    fun setCurrLedState(state : CurrStateLed){
        currLedState = state
    }
    fun getCurrLedState() : CurrStateLed {
        return currLedState
    }








}