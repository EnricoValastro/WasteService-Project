package transporttrolley.state

import com.google.gson.Gson

enum class CurrStateTrolley { IDLE, STOPPED, MOVING, PICKINGUP, DROPPINGOUT}

data class TransportTrolleyState(private var tTState : CurrStateTrolley){

    companion object {
        private val gson = Gson()
        fun fromJsonString(str : String) : TransportTrolleyState{
            return TransportTrolleyState.gson.fromJson(str, TransportTrolleyState::class.java)
        }
    }

    fun updateTTState(newState : CurrStateTrolley){
        tTState = newState
    }

    fun getTTState() : CurrStateTrolley{
        return tTState
    }

    fun toJsonString() : String{
        return TransportTrolleyState.gson.toJson(this)
    }
}
