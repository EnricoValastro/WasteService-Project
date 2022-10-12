package unibo.planner22.model;

public class RobotState {
	private int x;
	private int y;
	private Direction direction;
	
	public RobotState(int x, int y, Direction direction) {
		if (x<0 || y<0 || (direction != Direction.UP && 
				direction != Direction.RIGHT && direction != Direction.DOWN && 
				direction != Direction.LEFT))
			throw new IllegalArgumentException();
		this.x = x;
		this.y = y;
		this.direction = direction;
//		System.out.println("RobotState CREATEDDDDDDDDDDDDDDDD " + "x="+x + "y="+y + "direction=" + direction);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	@Override
	public int hashCode() {
		int result = x + 31*y;
		switch(direction) {
		case UP: result = result + 31*31*0; break;
		case RIGHT: result = result + 31*31*1; break;
		case DOWN: result = result + 31*31*2; break;
		case LEFT: result = result + 31*31*3; break;
		default: throw new IllegalArgumentException("Not a valid direction");
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this.getClass() != o.getClass())
			return false;
		RobotState state = (RobotState) o;
		return this.x == state.x && this.y == state.y && this.direction == state.direction;
	}
	
	public Direction getBackwardDirection() {
		switch(direction) {
		case UP: return Direction.DOWN;
		case RIGHT: return Direction.LEFT;
		case DOWN: return Direction.UP;
		case LEFT: return Direction.RIGHT;
		default: throw new IllegalArgumentException("Not a valid direction");
		}
	}
	
	public RobotState turnRight() {
		RobotState result = new RobotState(this.x, this.y, this.direction);
		switch(result.direction) {
		case UP: result.direction = Direction.RIGHT; break;
		case RIGHT: result.direction = Direction.DOWN; break;
		case DOWN: result.direction = Direction.LEFT; break;
		case LEFT: result.direction = Direction.UP; break;
		default: throw new IllegalArgumentException("Not a valid direction");
		}
		return result;
	}
	
	public RobotState turnLeft() {
		RobotState result = new RobotState(this.x, this.y, this.direction);
		switch(result.direction) {
		case UP: result.direction = Direction.LEFT; break;
		case RIGHT: result.direction = Direction.UP; break;
		case DOWN: result.direction = Direction.RIGHT; break;
		case LEFT: result.direction = Direction.DOWN; break;
		default: throw new IllegalArgumentException("Not a valid direction");
		}
		return result;
	}
	
	public RobotState forward() {
		RobotState result = new RobotState(this.x, this.y, this.direction);
		switch(result.direction) {
		case UP: result.y--; break;
		case DOWN: result.y++; break;
		case LEFT: result.x--; break;
		case RIGHT: result.x++; break;
		default: throw new IllegalArgumentException("Direction not valid");
		}
		return result;
	}
	
	public RobotState backward() {
		RobotState result = new RobotState(this.x, this.y, this.direction);
		switch(result.direction) {
		case UP: result.y++; break;
		case DOWN: result.y--; break;
		case LEFT: result.x++; break;
		case RIGHT: result.x--; break;
		default: throw new IllegalArgumentException("Direction not valid");
		}
		return result;
	}
	
	public enum Direction {
		UP, RIGHT, DOWN, LEFT;
	}

	public String toString() {
		return "("+x+","+y+")"+direction;
	}
}
