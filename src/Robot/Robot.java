package Robot;
import java.util.*;



public class Robot {
	private int xPos;
	private int yPos;
	private int heading;      //Direction of the robot 1, 2, 3, 4
	
	private ArrayList<Sensor> sensors = null;
	
	private int stepPerSecond;
	private int coverageLimit;
	private int timeLimit;
	private boolean reachCoverageLimit = false;
	private boolean reachTimeLimit = false;
	
	
	
	
	public Robot(int x, int y, int h){
		this.xPos = x;
		this.yPos = y;
		this.heading = h;
	}
	
	
	
	
	
}
