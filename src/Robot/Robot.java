package robot;
import java.util.*;



public class Robot {
	private int xPos;
	private int yPos;
	private int heading;      //Direction of the robot 1:right, 2:down, 3:left, 4:up
	
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
