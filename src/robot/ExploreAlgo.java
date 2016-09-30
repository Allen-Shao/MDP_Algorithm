package robot;

import map.Map;
import map.MapGrid;
import map.MapConstants;
import robot.Robot;
import robot.Sensor;
import robot.ShortestPathAlgo;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ExploreAlgo{

	private Map trueMap;
	private Map knownMap;
	private Robot expRobot;

	private double coverLimit;
	private int timeLimitInSecond;

	public ExploreAlgo(Map tMp, Map kMp, Robot r){
		this.trueMap = tMp;
		this.expRobot = r;

		this.knownMap = kMp;
		knownMap.addBorder();

		this.coverLimit = 1;
		this.timeLimitInSecond = 1000000; //no time limit
	}

	public ExploreAlgo(Map tMp, Map kMp, Robot r, double cL, int tL){
		this.trueMap = tMp;
		this.expRobot = r;

		this.knownMap = kMp;
		knownMap.addBorder();

		this.coverLimit = cL;
		this.timeLimitInSecond = tL;
	}


	public void runExploration(){

		ArrayList<Sensor> allSensors = expRobot.getSensors();

		boolean endFlag = false;
		boolean limitReached = false;

		int step = 0;
		// knownMap.repaint();
		while(!endFlag){


			knownMap.printExplorationProgress();
			knownMap.repaint();


			markCurrentPosition();
			sensorDetect();
			//finite state machine (make only one step per loop)

			if (!hasObstacleOnRight()){
				robotTurnRight();
				if (!hasObstacleInFront()){ //forward checking
					robotMoveForward();
					try{
						TimeUnit.MILLISECONDS.sleep(1000/expRobot.getSpeed());
					} catch(InterruptedException e){
						System.out.println("InterruptedException");
					}
					step++; //count one more step
				}
			} else if (!hasObstacleInFront()){
				robotMoveForward();
			} else if (!hasObstacleOnLeft()){
				robotTurnLeft();
			} else {
				robotTurnRight();
			}


			try{
				TimeUnit.MILLISECONDS.sleep(1000/expRobot.getSpeed());
			} catch(InterruptedException e){
				System.out.println("InterruptedException");
			}
			step++; //count the step

			//for debugging
			// Scanner sc = new Scanner(System.in);
			// System.out.println("Press any key to continue...");
			// sc.nextLine();


			//set for ending condition

			//1. go back to start point
			if (sameGrid(expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER)))
				endFlag = true;

			//2. reach cover limit

			//System.out.println(calculateCoverRate());
			if (calculateCoverRate() >= coverLimit){
				endFlag = true;
				limitReached = true;
			}


			//3. reach time limit
			if (step/expRobot.getSpeed() >= timeLimitInSecond){
				endFlag = true;
				limitReached = true;
				System.out.println(step);
			}

			

		}

		if (limitReached){
			//mark all the unexplored area as obstacle.
			//use shortest path to go back to the start point
			for (int i = 1; i < MapConstants.MAP_ROW-1; i++){
				for (int j = 1; j < MapConstants.MAP_COL-1; j++){
					if (!knownMap.getGrid(i, j).isExplored()){
						knownMap.addObstacle(i, j);
					}
				}
			}

			ShortestPathAlgo s = new ShortestPathAlgo(knownMap, expRobot, expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER));
			s.runShortestPath();

		}

		//System.out.println("Exploration Ends.");

		//trueMap.printMap();

		//return knownMap;
		

	}

	private void markCurrentPosition(){
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				MapGrid curPos = expRobot.getPosition();
				knownMap.getGrid(curPos.getRow()+i, curPos.getCol()+j).setExplored(true);
			}
		}
	}

	private void sensorDetect(){
		ArrayList<Sensor> sensors = expRobot.getSensors();
		MapGrid curPos = expRobot.getPosition();
		for (Sensor s : sensors){
			int detectDirection = getDetectDirection(s.getDirection(), expRobot.getHeading());
			int xtemp, ytemp;
			MapGrid sensorCurPos = getSensorCurrentPosition(s);
			//System.out.println(detectDirection);
			for (int i=0; i<=s.getRange();i++){
				switch (detectDirection){
					case 1:
						xtemp = sensorCurPos.getRow();
						ytemp = sensorCurPos.getCol()+i;


						if (detectCurrentGrid(xtemp, ytemp))
							i = s.getRange()+1;   //break the loop
						break;
					case 2:
						xtemp = sensorCurPos.getRow()-i;
						ytemp = sensorCurPos.getCol();


						if (detectCurrentGrid(xtemp, ytemp))
							i = s.getRange()+1;   //break the loop
						break;
					case 3:
						xtemp = sensorCurPos.getRow();
						ytemp = sensorCurPos.getCol()-i;


						if (detectCurrentGrid(xtemp, ytemp))
							i = s.getRange()+1;   //break the loop
						break;
					case 4:
						xtemp = sensorCurPos.getRow()+i;
						ytemp = sensorCurPos.getCol();


						if (detectCurrentGrid(xtemp, ytemp))
							i = s.getRange()+1;   //break the loop
						break;
				}
			}
		}

	}


	private boolean detectCurrentGrid(int x, int y){  //return whether the grid is a obstacle
		MapGrid trueGrid = trueMap.getGrid(x, y);
		knownMap.getGrid(x, y).setExplored(true);
		//System.out.println(trueGrid.isObstacle());
		if (trueGrid.isObstacle()){
			knownMap.addObstacle(x, y);
			return true;
		} else {
			return false;
		}

	}


	//Bad implementation
	private MapGrid getSensorCurrentPosition(Sensor s){
		int r, c; //row, col
		switch (expRobot.getHeading()){
			case 1:
				r = expRobot.getPosition().getRow()+s.getRow();
				c = expRobot.getPosition().getCol()+s.getCol();
				break;
			case 2:
				r = expRobot.getPosition().getRow()-s.getCol();
				c = expRobot.getPosition().getCol()+s.getRow();
				break;
			case 3:
				r = expRobot.getPosition().getRow()-s.getRow();
				c = expRobot.getPosition().getCol()-s.getCol();
				break;
			case 4:
				r = expRobot.getPosition().getRow()+s.getCol();
				c = expRobot.getPosition().getCol()-s.getRow();
				break;
			default:
				r = expRobot.getPosition().getRow();
				c = expRobot.getPosition().getCol();
				break;
		}
		return new MapGrid(r ,c);
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
		int newHeading = expRobot.getHeading()-1;
		if (newHeading == 0)
			newHeading = 4;
		expRobot.setHeading(newHeading);		
	}

	private void robotTurnRight(){
		int newHeading = (expRobot.getHeading()+1)%4;
		if (newHeading == 0)
			newHeading = 4;
		expRobot.setHeading(newHeading);		
	}

	private boolean hasObstacleInFront(){
		MapGrid curPos = expRobot.getPosition();
		MapGrid frontGrid;
		int curRow = curPos.getRow();
		int curCol = curPos.getCol();
		int curHeading = expRobot.getHeading();
		switch (curHeading){
			case 1:
				frontGrid = knownMap.getGrid(curRow, curCol+1);
				return !frontGrid.isExplored() || frontGrid.isObstacle() || frontGrid.isVirtualWall();
			case 2:
				frontGrid = knownMap.getGrid(curRow-1, curCol);
				return !frontGrid.isExplored() || frontGrid.isObstacle() || frontGrid.isVirtualWall();
			case 3:
				frontGrid = knownMap.getGrid(curRow, curCol-1);
				return !frontGrid.isExplored() || frontGrid.isObstacle() || frontGrid.isVirtualWall();
			case 4:
				frontGrid = knownMap.getGrid(curRow+1, curCol);
				return !frontGrid.isExplored() || frontGrid.isObstacle() || frontGrid.isVirtualWall();
		}
		return true;
	}

	private boolean hasObstacleOnLeft(){
		MapGrid curPos = expRobot.getPosition();
		MapGrid leftGrid;
		int curRow = curPos.getRow();
		int curCol = curPos.getCol();
		int curHeading = expRobot.getHeading();
		switch (curHeading){
			case 1:
				leftGrid = knownMap.getGrid(curRow+1, curCol);
				return !leftGrid.isExplored() || leftGrid.isObstacle() || leftGrid.isVirtualWall();
			case 2:
				leftGrid = knownMap.getGrid(curRow, curCol+1);
				return !leftGrid.isExplored() || leftGrid.isObstacle() || leftGrid.isVirtualWall();
			case 3:
				leftGrid = knownMap.getGrid(curRow-1, curCol);
				return !leftGrid.isExplored() || leftGrid.isObstacle() || leftGrid.isVirtualWall();
			case 4:
				leftGrid = knownMap.getGrid(curRow, curCol-1);
				return !leftGrid.isExplored() || leftGrid.isObstacle() || leftGrid.isVirtualWall();
		}
		return true;
	}

	private boolean hasObstacleOnRight(){
		MapGrid curPos = expRobot.getPosition();
		MapGrid rightGrid;
		int curRow = curPos.getRow();
		int curCol = curPos.getCol();
		int curHeading = expRobot.getHeading();
		switch (curHeading){
			case 1:
				rightGrid = knownMap.getGrid(curRow-1, curCol);
				return !rightGrid.isExplored() || rightGrid.isObstacle() || rightGrid.isVirtualWall();
			case 2:
				rightGrid = knownMap.getGrid(curRow, curCol-1);
				return !rightGrid.isExplored() || rightGrid.isObstacle() || rightGrid.isVirtualWall();
			case 3:
				rightGrid = knownMap.getGrid(curRow+1, curCol);
				return !rightGrid.isExplored() || rightGrid.isObstacle() || rightGrid.isVirtualWall();
			case 4:
				rightGrid = knownMap.getGrid(curRow, curCol+1);
				return !rightGrid.isExplored() || rightGrid.isObstacle() || rightGrid.isVirtualWall();
		}
		return true;
	}

	private double calculateCoverRate(){
		double exploredNumber = 0.0;
		for (int i = 1; i < MapConstants.MAP_ROW-1; i++){
			for (int j = 1; j < MapConstants.MAP_COL-1; j++){
				if (knownMap.getGrid(i, j).isExplored()){
					exploredNumber++;
				}
			}
		}
		return exploredNumber/((MapConstants.MAP_ROW-2) * (MapConstants.MAP_COL-2));
	}

	private boolean sameGrid(MapGrid a, MapGrid b){
		return (a.getCol() == b.getCol()) && (a.getRow() == b.getCol());
	}


}