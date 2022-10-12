package unibo.planner22;

import java.util.List;
import aima.core.agent.Action;
import unibo.kotlin.planner22Util;


public class MainPlannerdemo {

	protected void println(String m){
		System.out.println(m);
	}

	public void doJob1() {
		try {
			planner22Util.initAI();
 			planner22Util.showMap();
			planner22Util.startTimer();
			for( int i=1; i<=4; i++) {
				List<Action> actions = planner22Util.doPlan();
				println("===== plan actions for next move: " + actions);
				executeMoves();
				println("===== map after plan");
				;
				planner22Util.showMap();
			}
			planner22Util.getDuration();
		}catch ( Exception e) {
			 e.printStackTrace();
		}
	}
	public void doJob() {
 		try {
			planner22Util.initAI();
 			planner22Util.showMap();
			planner22Util.startTimer();
			doSomeMOve();
			println("===== map after some move");;
			planner22Util.showMap();


			//Planner22Util.cell0DirtyForHome()
			planner22Util.setGoal(0,0);
			planner22Util.doPlan();
			executeMoves( );
			println("===== map after plan for home");;
			planner22Util.showMap();

			planner22Util.getDuration();

		} catch ( Exception e) {
			//e.printStackTrace()
		}

	}

	protected void doSomeMOve(){
        planner22Util.doMove("w","");
        planner22Util.doMove("a","");
        planner22Util.doMove("w","");
        planner22Util.doMove("w","");
        planner22Util.doMove("d","");
        planner22Util.doMove("w","");
        planner22Util.doMove("a","");
        planner22Util.doMove("obstacleOnRight","");

	}
	protected void executeMoves(){
        String move = planner22Util.getNextPlannedMove();
        while ( move.length() > 0 ) {
            planner22Util.doMove( move ,"");
			move = planner22Util.getNextPlannedMove();
        }
	}
	public static void main( String[] args) throws Exception {
 		MainPlannerdemo appl = new MainPlannerdemo( );
		appl.doJobWaste();
		//appl.terminate();
	}

	public void doJobWaste() {
		try {
			planner22Util.initAI();
			planner22Util.setWallRight(8+1,10);
			planner22Util.setWallDown(10+1,8);
			println(planner22Util.getMap());
			planner22Util.updateMap("w", "" );
			println(planner22Util.getMap());
			planner22Util.updateMap("w", "stop" );
			println(planner22Util.getMap());
			planner22Util.updateMap("w", "" );
			println(planner22Util.getMap());
			planner22Util.cleanMyRoom();
			println(planner22Util.getMap());
		} catch ( Exception e) {
			//e.printStackTrace()
		}

	}


}
