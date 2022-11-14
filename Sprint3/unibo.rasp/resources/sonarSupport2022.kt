import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.radarSystem22.domain.interfaces.ISonar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class sonarSupport2022(name : String ) : ActorBasic( name ) {

    lateinit var sonar : ISonar

    override suspend fun actorBody(msg: IApplMessage) {

        if( msg.msgId() == "sonaractivate"){

            sonar = `it.unibo`.radarSystem22.domain.DeviceFactory.createSonar()
            sonar.activate()
            doRead()
        }
        if( msg.msgId() == "sonardeactivate"){
            sonar.deactivate()
        }
    }

    suspend fun doRead(   ){
        var data = 0
        GlobalScope.launch{	//to allow message handling
            while( sonar.isActive ){
                data = sonar.distance.`val`
                try{
                    if( data <= 100 ){	//A first filter ...
                        val m1 = "distance( ${data} )"
                        val event = MsgUtil.buildEvent( "sonarSupport2022","sonar",m1)

                        emitLocalStreamEvent( event )		//not propagated to remote actors
                        println("sonarSupport2022 doRead: $event "   )
                    }
                }catch(e: Exception){
                    println("sonarSupport2022 doRead ERROR: $e "   )
                }
                delay( 800 ) 	//Avoid too fast generation
            }
        }
    }
}