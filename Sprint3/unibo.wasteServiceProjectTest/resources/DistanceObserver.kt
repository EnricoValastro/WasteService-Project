import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.radarSystem22.domain.interfaces.IObserver
import kotlinx.coroutines.runBlocking
import java.util.*

class DistanceObserver( val owner: ActorBasic) : IObserver {
    override fun update(d: String?) {
        //val v = d!!.toInt()
        println("DistanceObserver d=$d")
        val m1 = "distance( ${d} )"
        val event = MsgUtil.buildEvent( "distanceObs","sonar",m1)
        runBlocking{
            //MsgUtil.sendMsg(event, owner)     //Evento percepito solo da owner
            owner.emit(event)                   //Evento percepito da tutti gli attori del sistema
        }
    }

    override fun update(o: Observable?, data: Any?) {
        update(data.toString() );
    }
}