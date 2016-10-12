package robot;

import java.util.ArrayList;

import map.MapGrid;

import leaderboard.CommMgr;
import leaderboard.CommConstants;


public class Robot {
	private MapGrid position;
	private int heading;      //Direction of the robot 1:right, 2:down, 3:left, 4:up
	private int speed;        //steps per second


	private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	
	
	public Robot(MapGrid pos, int h){
		this.position = pos;
		this.heading = h;
		this.speed = 5;
	}

	public MapGrid getPosition(){
		return this.position;
	}

	public int getHeading(){
		return this.heading;
	}

	public ArrayList<Sensor> getSensors(){
		return this.sensors;
	}

	public int getSpeed(){
		return this.speed;
	}

	public void setPosition(MapGrid pos){
		this.position = pos;
	}

	public void setHeading(int h){
		this.heading = h;
	}

	public void setSpeed(int s){
		this.speed = s;
	}

	public void addSensor(Sensor s){
		sensors.add(s);
	}

	public void removeSensor(Sensor s){
		sensors.remove(s);
	}


	public void calibrate(String msg){
		CommMgr.getCommMgr().sendMsg(msg, CommConstants.MSG_TO_ARDUINO);
		String ack = CommMgr.getCommMgr().recvMsg();
		if (ack.equals("Done")){
			System.out.println("Calibration completed!");
		}
	}
	
	
	
}
