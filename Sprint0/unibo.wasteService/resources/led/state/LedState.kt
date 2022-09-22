package led.state

import com.google.gson.Gson

enum class CurrStateLed { ON, OFF, BLINKING }

data class LedState(
       private var currState : CurrStateLed
){

    companion object {
        private val gson = Gson()
        fun fromJsonString(str : String) : LedState {
            return LedState.gson.fromJson(str, LedState::class.java)
        }
    }

    fun updateLedState(newState : CurrStateLed){
        currState = newState
    }

    fun getTTState() : CurrStateLed {
        return currState
    }

    fun toJsonString() : String{
        return LedState.gson.toJson(this)
    }

}
