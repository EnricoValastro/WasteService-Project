package unibo.planner22.model;

import java.io.Serializable;

//import java.io.Serializable;

public class Box implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isObstacle;
	private boolean isDirty;
	private boolean isRobot;
	private boolean isRobotStop;
	
	public Box(boolean isObstacle, boolean isDirty, boolean isRobot, boolean isRobotStop) {
		this.isObstacle = isObstacle;
		this.isDirty    = isDirty;
		this.isRobot    = isRobot;
		this.isRobotStop = isRobotStop;
	}
	
	public Box(boolean isObstacle, boolean isDirty,boolean isRobotStop) {this(isObstacle, isDirty, true, isRobotStop);
	}
	
	public Box() {
		this(false, true,true);
	}
	
	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}
	
	public boolean isRobot() {
		return this.isRobot;
	}
	
	public boolean isObstacle() {
		return this.isObstacle;
	}
	
	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	public boolean isRobotStop(){return this.isRobotStop;}

	public void setRobotStop(boolean isRobotStop){this.isRobotStop = isRobotStop;}
}
