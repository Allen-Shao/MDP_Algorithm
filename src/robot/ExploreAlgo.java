package robot;

import map.Map;
import map.MapGrid;
import map.MapConstants;
import robot.Robot;

import java.util.*;

public class ExploreAlgo{

	private Map trueMap;
	private Map knownMap;
	private Robot expRobot;

	public ExploreAlgo(Map tMp, Robot r){
		this.trueMap = tMp;
		this.expRobot = r;

		this.knownMap = new Map();
		knownMap.addBorder();
	}

	public Map runExploration(){

		ArrayList<Sensor> allSensors = expRobot.getSensors();

		while(true){

		}
		

	}

	private void sensorDetect(ArrayList<Sensor> sensors){
		MapGrid curPos = expRobot.getPosition();
		for (Sensor s : sensors){
			int detectDirection = getDetectDirection(s.getDirection(), expRobot.getHeading());
			int xtemp, ytemp;
			MapGrid sensorCurPos = 
			for (int i=1; i<=s.getRange();i++){
				switch (detectDirection){
					case 1:
						xtemp = curPos.getRow()+s.getRow();
						ytemp = curPos.getCol()+s.getCol()+i;
						knownMap.getGrid(xtemp, ytemp) = trueMap.getGrid(xtemp, ytemp);
						break;
					case 2:
						xtemp = curPos.getRow()-s.getRow()-i;
						ytemp = curPos.getCol()+s.getCol();
						knownMap.getGrid(xtemp, ytemp) = trueMap.getGrid(xtemp, ytemp);
						break;
					case 3:
						xtemp = curPos.getRow()+s.getRow();
						ytemp = curPos.getCol()-s.getCol()-i;
						knownMap.getGrid(xtemp, ytemp) = trueMap.getGrid(xtemp, ytemp);
						break;
					case 4:
						xtemp = curPos.getRow()+s.getRow()+i;
						ytemp = curPos.getCol()+s.getCol();
						knownMap.getGrid(curPos.getRow()+i, curPos.getCol()) = 
						trueMap.getGrid(curPos.getRow()+i, curPos.getCol());
						break;
				}
			}
		}

	}



	private MapGrid getSensorCurrentPosition(Sensor s){
		int r, c; //row, col
		switch (expRobot.getHeading()){
			case 1:
				r = expRobot.getRow()+s.getRow();
				c = expRobot.getCol()+s.getCol();
				break;
			case 2:
				r = expRobot.getRow()-s.getCol();
				c = expRobot.getCol()+s.getRow();
				break;
			case 3:
				r = expRobot.getRow()-s.getRow();
				c = expRobot.getCol()-s.getCol();
				break;
			case 4:
				r = expRobot.getRow()+s.getCol();
				c = expRobot.getCol()-s.getRow();
				break;
		}
		return new MapGrid(r,c);
	}



	private int getDetectDirection(int sHeading, int rHeading){
		int detectDirection = (sHeading+rHeading-1) % 4;
		if (detectDirection == 0)
			return 4;
		else
			return detectDirection;
	}

	private void robotMoveForward(){
		MapGrid curPos = expRobot.getPosition();
		switch (expRobot.getHeading()){
			case 1:
				expRobot.setPosition(knownMap.getGrid(curPos.getRow(), curPos.getCol()+1));
				break;
			case 2:
				expRobot.setPosition(knownMap.getGrid(curPos.getRow()-1, curPos.getCol()));
				break;
			case 3:
				expRobot.setPosition(knownMap.getGrid(curPos.getRow(), curPos.getCol()-1));
				break;
			case 4:
				expRobot.setPosition(knownMap.getGrid(curPos.getRow()+1, curPos.getCol()));
				break;
		}
	}

	private void robotTurnLeft(){
		int newHeading = expRobot.getPosition()-1;
		if (newHeading == 0)
			newHeading = 4;
		expRobot.setHeading(newHeading);		
	}

	private void robotTurnRight(){
		int newHeading = (expRobot.getPosition()+1)%4;
		expRobot.setHeading(newHeading);		
	}




}