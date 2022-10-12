package unibo.planner22.model;

import java.util.HashSet;
import java.util.Set;
import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.StepCostFunction;

public class Functions implements ActionsFunction, ResultFunction, StepCostFunction, GoalTest {
	public static final double MOVECOST = 1.0;
	public static final double TURNCOST = 1.0;

	@Override
	public double c(Object arg0, Action arg1, Object arg2) {
		RobotAction action = (RobotAction) arg1;
		if (action.getAction() == RobotAction.FORWARD || action.getAction() == RobotAction.BACKWARD)
			return MOVECOST;
		else
			return TURNCOST;
	}

	@Override
	public Object result(Object arg0, Action arg1) {
		RobotState state = (RobotState) arg0;
		RobotAction action = (RobotAction) arg1;
		RobotState result;
		
		switch(action.getAction()) {
		case RobotAction.FORWARD:   result = state.forward(); break;
		case RobotAction.BACKWARD:  result = state.backward(); break;
		case RobotAction.TURNLEFT:  result = state.turnLeft(); break;
		case RobotAction.TURNRIGHT: result = state.turnRight(); break;
		default: throw new IllegalArgumentException("Not a valid RobotAction");
		}
		return result;
	}

	@Override
	public Set<Action> actions(Object arg0) {
		RobotState state = (RobotState) arg0;
		Set<Action> result = new HashSet<>();
		
		result.add(new RobotAction(RobotAction.TURNLEFT));
		result.add(new RobotAction(RobotAction.TURNRIGHT));
		
		if (RoomMap.getRoomMap().canMove(state.getX(), state.getY(), state.getDirection()))
			result.add(new RobotAction(RobotAction.FORWARD));
		
		return result;
	}

	@Override
	public boolean isGoalState(Object arg0) {
		RobotState state = (RobotState) arg0;
		System.out.println("				Functions check if is dirty and not obstacle: " + (unibo.planner22.model.RobotState) arg0);
		if (RoomMap.getRoomMap().isDirty(state.getX(), state.getY()) &&
				!RoomMap.getRoomMap().isObstacle(state.getX(), state.getY())) {
			System.out.println("				Functions isGoalState true : " + (unibo.planner22.model.RobotState) arg0);
			return true;
		}else {
			System.out.println("				Functions isGoalState false: " + (unibo.planner22.model.RobotState) arg0);
			return false;
		}
	}

}
