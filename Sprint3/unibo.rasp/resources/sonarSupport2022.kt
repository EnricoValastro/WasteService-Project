import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.radarSystem22.domain.interfaces.ISonar
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig
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
        var m1 = ""
        var lastSonarVal : Int = sonar.distance.`val`
        lateinit var event : IApplMessage
        var data = 0
        GlobalScope.launch{	//to allow message handling
            while( sonar.isActive ){
                data = sonar.distance.`val`
                if(data != lastSonarVal){	//A first filter ...
                    lastSonarVal = data
                    m1 = "distance( ${data*2} )"
                    event = MsgUtil.buildEvent( "sonarSupport2022","sonar",m1)
                    emitLocalStreamEvent( event )		//not propagated to remote actors
                    println("sonarSupport2022   |   read: $data, propagate: ${data*2} ")
                }

                delay( 500 ) //avoid to fast generation
            }
        }
    }
}