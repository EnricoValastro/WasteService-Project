package robotLegoNano
/*
-------------------------------------------------------------------------------
WARNING: this version makes reference to the library labbaseRobotSam.jar
 -------------------------------------------------------------------------------
 */
/*
import it.unibo.iot.baseRobot.hlmodel.BasicRobot
import it.unibo.iot.models.commands.baseRobot.BaseRobotSpeed
import it.unibo.iot.models.commands.baseRobot.BaseRobotSpeedValue
import it.unibo.iot.models.commands.baseRobot.BaseRobotStop
import it.unibo.iot.models.commands.baseRobot.BaseRobotForward
import it.unibo.iot.models.commands.baseRobot.BaseRobotBackward
import it.unibo.iot.models.commands.baseRobot.BaseRobotLeft
import it.unibo.iot.models.commands.baseRobot.BaseRobotRight
import it.unibo.iot.models.commands.baseRobot.IBaseRobotCommand
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ActorBasic
import itunibo.robotRaspOnly.sonarHCSR04Support
 
object nanoSupport {
	
	val SPEED_LOW     = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_LOW)
	val SPEED_MEDIUM  = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_MEDIUM)
	val SPEED_HIGH    = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_HIGH)
	 
	val basicRobot    = BasicRobot.getRobot()
	val robot         = basicRobot.getBaseRobot()
 	
	fun create(actor: ActorBasic, withSonar : Boolean = true){
		println("nanoSupport CREATING $robot withSonar=$withSonar")
		if(withSonar) sonarHCSR04Support.create( actor, " ")
		else println("nanoSupport CREATING $robot with no Sonar ")
	} 
	
	fun move( cmd : String ){
		println( "nanoSupport move $cmd $robot" )
		var command : IBaseRobotCommand = BaseRobotStop(SPEED_LOW )
		when( cmd ){
			"msg(w)" -> command = BaseRobotForward( SPEED_HIGH )
			"msg(s)" -> command = BaseRobotBackward(SPEED_HIGH )
			"msg(a)" -> command = BaseRobotLeft(SPEED_MEDIUM )
			"msg(d)" -> command = BaseRobotRight(SPEED_MEDIUM )
			"msg(h)" -> command = BaseRobotStop(SPEED_LOW )
		}
		robot.execute(command)
	}
}
 		*/