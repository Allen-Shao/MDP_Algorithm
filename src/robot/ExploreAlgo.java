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
			int detectDirection = (s.getDirection()+expRobot.getHeading()-1) % 4;
			if (detectDirection == 0)
				detectDirection = 4;
			for (int i=1; i<=s.getRange();i++){
				switch (expRobot.getHeading()){
					case 1:
						knownMap.getGrid(curPos.getRow(), curPos.getCol()+i) = 
						trueMap.getGrid(curPos.getRow(), curPos.getCol()+i);
						break;
					case 2:
						knownMap.getGrid(curPos.getRow()-i, curPos.getCol()) = 
						trueMap.getGrid(curPos.getRow()-i, curPos.getCol());
						break;
					case 3:
						knownMap.getGrid(curPos.getRow(), curPos.getCol()-i) = 
						trueMap.getGrid(curPos.getRow(), curPos.getCol()-i);
						break;
					case 4:
						knownMap.getGrid(curPos.getRow()+i, curPos.getCol()) = 
						trueMap.getGrid(curPos.getRow()+i, curPos.getCol());
						break;
				}
			}
		}

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