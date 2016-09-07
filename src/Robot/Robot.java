package robot;

import map.*;
import java.util.*;



public class Robot {
	private MapGrid position;
	private int heading;      //Direction of the robot 1:right, 2:down, 3:left, 4:up
	
	private ArrayList<Sensor> sensors = null;
	
	
	
	
	public Robot(MapGrid pos, int h){
		this.postion = pos;
		this.heading = h;
	}

	public MapGrid getPositon(){
		return this.postion;
	}
	
	
	
	
	
}
