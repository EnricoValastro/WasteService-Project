package  unibo.kotlin

import java.util.ArrayList
import aima.core.agent.Action
import aima.core.search.framework.SearchAgent
import aima.core.search.framework.problem.GoalTest
import aima.core.search.framework.problem.Problem
import aima.core.search.framework.qsearch.GraphSearch
import aima.core.search.uninformed.BreadthFirstSearch
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream
import unibo.planner22.model.*

object planner22Util {
    private var robotState: RobotState? = null
	private var actions: List<Action>?    = null
 	
    private var curPos    : Pair<Int,Int> = Pair(0,0)
	private var curDir    : RobotState.Direction  = RobotState.Direction.DOWN
    private var curGoal: GoalTest = Functions()		 

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
 	
	private var direction             = "downDir"
	private	var currentGoalApplicable = true;

	private var actionSequence : Iterator<Action>? = null

	private var storedactionSequence : Iterator<Action>? = null
    private var storedPos  : Pair<Int,Int> = Pair(0,0)

    private var search: BreadthFirstSearch? = null
    private var timeStart: Long = 0
	
	
/* 
 * ------------------------------------------------
 * CREATE AND MANAGE PLANS
 * ------------------------------------------------
 */
    @Throws(Exception::class)
    @JvmStatic fun initAI() {
         robotState = RobotState(0, 0, RobotState.Direction.DOWN)
         search     = BreadthFirstSearch(GraphSearch())
	     println("Planner22Util initAI done")
    }

    @JvmStatic
	fun setGoal( x: Int, y: Int) {
        try {
             println("setGoal $x,$y while robot in cell: ${getPosX()},${getPosY()} direction=${getDirection()} ") //canMove=$canMove

			if( RoomMap.getRoomMap().isObstacle(x,y) ) {
				println("ATTEMPT TO GO INTO AN OBSTACLE ")
				currentGoalApplicable = false
 				resetActions()
				return
			}else currentGoalApplicable = true

			RoomMap.getRoomMap().put(x, y, Box(false, true, false))  //set dirty

			curGoal = GoalTest { state  : Any ->
                val robotState = state as RobotState
				(robotState.x == x && robotState.y == y)
            }
			showMap()
         } catch (e: Exception) {
             //e.printStackTrace()
     		}
    }
 
    @Throws(Exception::class)
    @JvmStatic
	fun doPlan(): List<Action>? {
		//println("Planner22Util doPlan curGoal=$curGoal" )
		
		if( ! currentGoalApplicable ){
			println("Planner22Util doPlan cannot go into an obstacle")
			actions = listOf<Action>()
			return actions		//empty list
		} 
		
        val searchAgent: SearchAgent
        //println("Planner22Util doPlan newProblem (A) $curGoal" );
		val problem = Problem(robotState, Functions(), Functions(), curGoal, Functions())
        //println("Planner22Util doPlan newProblem (A) search " );
        searchAgent = SearchAgent(problem, search!!)
        actions     = searchAgent.actions
		
		println("Planner22Util doPlan actions=$actions")
		
        if (actions == null || actions!!.isEmpty()) {
            println("Planner22Util doPlan NO MOVES !!!!!!!!!!!! $actions!!"   )
            if (!RoomMap.getRoomMap().isClean) RoomMap.getRoomMap().setObstacles()
            //actions = ArrayList()
            return null
        } else if (actions!![0].isNoOp) {
            println("Planner22Util doPlan NoOp")
            return null
        }
		
        //println("Planner22Util doPlan actions=$actions")
		setActionMoveSequence()
        return actions
    }
	
	@JvmStatic fun planForGoal( x: String, y: String) {
		val vx = Integer.parseInt(x)
		val vy = Integer.parseInt(y)
		setGoal(vx,vy)		
		doPlan()   
 	}	
  	
	@JvmStatic fun planForNextDirty() {
		val rmap = RoomMap.getRoomMap()
		for( i in 0..getMapDimX( )-1 ){
			for( j in 0..getMapDimY( )-1 ){
				//println( ""+ i + "," + j + " -> " + rmap.isDirty(i,j)   );
				if( rmap.isDirty(i,j)  ){
					setGoal( i,j )
					doPlan() 
					return
				}
			}
		}
 	}	


	@JvmStatic fun memoCurentPlan(){
		storedPos            = curPos
		storedactionSequence = actionSequence;
	}
	
	@JvmStatic fun restorePlan(){
		//Goto storedcurPos
		actionSequence = storedactionSequence;
	}
	
/*
 * ------------------------------------------------
 * MANAGE PLANS AS ACTION SEQUENCES
 * ------------------------------------------------
*/	
	@JvmStatic fun setActionMoveSequence(){
		if( actions != null ) {
			 actionSequence = actions!!.iterator()
		}
	}
	
	@JvmStatic fun getNextPlannedMove() : String{
		if( actionSequence == null ) return ""
		else if( actionSequence!!.hasNext()) return actionSequence!!.next().toString()
				else return ""
	}

	@JvmStatic fun getActions() : List<Action>{
        return actions!!
    }
	@JvmStatic fun existActions() : Boolean{
		//println("existActions ${actions!!.size}")
		return actions!!.size>0   
	}
	@JvmStatic fun resetActions(){
		actions = listOf<Action>()
	}
	
	@JvmStatic fun get_actionSequence() : Iterator<Action>?{
		return actionSequence
	}
 	
/*
 * ------------------------------------------------
 * INSPECTING ROBOT POSITION AND DIRECTION
 * ------------------------------------------------
*/		
	@JvmStatic fun get_curPos() : Pair<Int,Int>{
		return curPos
	}

	@JvmStatic fun getPosX() : Int{ return robotState!!.getX() }
    @JvmStatic fun getPosY() : Int{ return robotState!!.getY() }
	
	@JvmStatic fun getDir()  : RobotState.Direction{
		return robotState!!.getDirection()
	}

	@JvmStatic fun getDirection() : String{
 		val direction = getDir()
		when( direction ){
			RobotState.Direction.UP    -> return "upDir"
			RobotState.Direction.RIGHT -> return "rightDir"
			RobotState.Direction.LEFT  -> return "leftDir"
			RobotState.Direction.DOWN  -> return "downDir"
			else            -> return "unknownDir"
 		}
  	}
	
	@JvmStatic fun atHome() : Boolean{
		return curPos.first == 0 && curPos.second == 0
	}
	
	@JvmStatic fun atPos( x: Int, y: Int ) : Boolean{
		return curPos.first == x && curPos.second == y
	}
	
	@JvmStatic fun showCurrentRobotState(){
		println("===================================================")
		showMap()
		direction = getDirection()
		println("RobotPos=(${curPos.first}, ${curPos.second})  direction=$direction  " )  
		println("===================================================")
	}

/*
* ------------------------------------------------
* MANAGING THE ROOM MAP
* ------------------------------------------------
*/	
 	@JvmStatic fun getMapDimX( ) 	: Int{ return mapDims.first }
	@JvmStatic fun getMapDimY( ) 	: Int{ return mapDims.second }
	@JvmStatic fun mapIsEmpty() : Boolean{return (getMapDimX( )==0 &&  getMapDimY( )==0 ) }

	@JvmStatic fun showMap() {
        println(RoomMap.getRoomMap().toString() )
    }
	@JvmStatic fun getMap() : String{
		return RoomMap.getRoomMap().toString() 
	}
	@JvmStatic fun getMapOneLine() : String{ 
		return  "'"+RoomMap.getRoomMap().toString().replace("\n","@").replace("|","").replace(",","") +"'" 
	}

	@JvmStatic fun getMapDims() : Pair<Int,Int> {
		if( RoomMap.getRoomMap() == null ){
			return Pair(0,0)
		}
	    val dimMapx = RoomMap.getRoomMap().getDimX()
	    val dimMapy = RoomMap.getRoomMap().getDimY()
	    //println("getMapDims dimMapx = $dimMapx, dimMapy=$dimMapy")
		return Pair(dimMapx,dimMapy)	
	}
 	
	@JvmStatic fun loadRoomMap( fname: String  )   {
 		try{
 			val inps = ObjectInputStream(FileInputStream("${fname}.bin"))
			val map  = inps.readObject() as RoomMap;
 			println("loadRoomMap = $fname DONE")
			RoomMap.setRoomMap( map )
		}catch(e:Exception){			
			println("loadRoomMap = $fname FAILURE ${e.message}")
		}
		mapDims = getMapDims()//Pair(dimMapx,dimMapy)
	}
	
 
	@JvmStatic fun saveRoomMap(  fname : String)   {		
        println("saveMap in $fname")
		val pw = PrintWriter( FileWriter(fname+".txt") )
		pw.print( RoomMap.getRoomMap().toString() )
		pw.close()
		
		val os = ObjectOutputStream( FileOutputStream(fname+".bin") )
		os.writeObject(RoomMap.getRoomMap())
		os.flush()
		os.close()
		mapDims = getMapDims()
    }

/*
* ------------------------------------------------
* UPDATING THE ROOM MAP
* ------------------------------------------------
*/		
 	@JvmStatic fun updateMap( move: String, msg: String="" ){
		doMove( move )
		setPositionOnMap( )
		if( msg.length > 0 ) println(msg) 
 	}
	
	@JvmStatic fun updateMapObstacleOnCurrentDirection(   ){
		doMove( direction )
		setPositionOnMap( )
	}
	
	@JvmStatic fun setPositionOnMap( ){
		direction     =  getDirection()
		val posx      =  getPosX()
		val posy      =  getPosY()
		curPos        =  Pair( posx,posy )
	}
 	
    @JvmStatic fun doMove(move: String) {
        val x   = getPosX()  
        val y   = getPosY()  
		val map = RoomMap.getRoomMap()
       // println("Planner22Util: doMove move=$move  dir=$dir x=$x y=$y dimMapX=$dimMapx dimMapY=$dimMapy")
       try {
            when (move) {
                "w" -> {
                    //map.put(x, y, Box(false, false, false)) //clean the cell
					map.cleanCell(x,y)
                    robotState = Functions().result(robotState!!, RobotAction.wAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "s" -> {
                    robotState = Functions().result(robotState!!, RobotAction.sAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "a"  -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "l" -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "d" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "r" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
				//Used by WALL-UPDATING
				//Box(boolean isObstacle, boolean isDirty, boolean isRobot)
                "rightDir" -> map.put(x + 1, y, Box(true, false, false)) 
                "leftDir"  -> map.put(x - 1, y, Box(true, false, false))
                "upDir"    -> map.put(x, y - 1, Box(true, false, false))
                "downDir"  -> map.put(x, y + 1, Box(true, false, false))
		   }//when
        } catch (e: Exception) {
            println("Planner22Util doMove: ERROR:" + e.message)
        }
    }
	
	@JvmStatic fun moveRobotInTheMap(){
		RoomMap.getRoomMap().put(robotState!!.x, robotState!!.y, Box(false, false, true))
	}
	
/*
* ------------------------------------------------
* UPDATING THE ROOM MAP FOR WALLS
* ------------------------------------------------
*/		
 	@JvmStatic fun setObstacleWall(  dir: RobotState.Direction, x:Int, y:Int){
		if( dir == RobotState.Direction.DOWN  ){ RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false)) }
		if( dir == RobotState.Direction.RIGHT ){ RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) }
	}
	@JvmStatic fun wallFound(){
 		 val dimMapx = RoomMap.getRoomMap().getDimX()
		 val dimMapy = RoomMap.getRoomMap().getDimY()
		 val dir     = getDir()
		 val x       = getPosX()
		 val y       = getPosY()
		 setObstacleWall( dir,x,y )
 		 println("Planner22Util wallFound dir=$dir  x=$x  y=$y dimMapX=$dimMapx dimMapY=$dimMapy");
		 doMove( dir.toString() )  //set cell
  		 if( dir == RobotState.Direction.RIGHT) setWallDown(dimMapx,y)
		 if( dir == RobotState.Direction.UP)    setWallRight(dimMapy,x)
 	}
	@JvmStatic fun setWallDown(dimMapx: Int, y: Int ){
		 var k   = 0
		 while( k < dimMapx ) {
			RoomMap.getRoomMap().put(k, y+1, Box(true, false, false))
			k++
		 }		
	}	
	@JvmStatic fun setWallRight(dimMapy: Int, x: Int){
 		 var k   = 0
		 while( k < dimMapy ) {
			RoomMap.getRoomMap().put(x+1, k, Box(true, false, false))
			k++
		 }		
	}

	
/*
* ------------------------------------------------
* TIME UTILITIES
* ------------------------------------------------
*/		
    @JvmStatic fun startTimer() {
        timeStart = System.currentTimeMillis()
    }
	
    @JvmStatic fun getDuration() : Int{
        val duration = (System.currentTimeMillis() - timeStart).toInt()
		println("DURATION = $duration")
		return duration
    }
	
	
}
