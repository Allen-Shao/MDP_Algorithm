package robot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import map.Map;
import map.MapConstants;
import map.MapGrid;

import leaderboard.*;

public class ExploreAlgo{

	private Map trueMap;
	private Map knownMap;
	private Robot expRobot;

	private double coverLimit;
	private int timeLimitInSecond;
	private CommMgr commMgr = CommMgr.getCommMgr();

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


		boolean endFlag = false;
		boolean leaveStart = false;
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
			if (sameGrid(expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER))&&leaveStart)
				endFlag = true;

			if (!sameGrid(expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER))){
				leaveStart = true;
			}
	

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

		//System.out.println("Exploration Ends.");

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

		//trueMap.printMap();		

	}

	public void runRealExploration(){

		System.out.println("Start real Exploration");
		
		if (commMgr.isConnected()){
			//ArrayList<Sensor> allSensors = expRobot.getSensors();

			boolean endFlag = false;
			//boolean limitReached = false;
			boolean leaveStart = false;

			//int step = 0;
			// knownMap.repaint();
			knownMap.printExplorationProgress();
			knownMap.repaint();
			while(!endFlag){


				markCurrentPosition();
				updateSensorsReading();
				//calibration
				if (shouldCalibration()){
					commMgr.sendMsg(CommConstants.ROBOT_CALIBRATION, CommConstants.MSG_TO_ARDUINO);
					String ack = commMgr.recvMsg();
					System.out.println(ack);
				}




				//finite state machine (make only one step per loop)
				if (!hasObstacleOnRight()){
					robotTurnRight();
					commMgr.sendMsg(CommConstants.ROBOT_TURN_RIGHT, CommConstants.MSG_TO_ARDUINO);
					if (!hasObstacleInFront()){ //forward checking
						try{
							TimeUnit.MILLISECONDS.sleep(1000);
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						robotMoveForward();
						commMgr.sendMsg(CommConstants.ROBOT_MOVE_FORWARD, CommConstants.MSG_TO_ARDUINO);
					}
				} else if (!hasObstacleInFront()){
					robotMoveForward();
					commMgr.sendMsg(CommConstants.ROBOT_MOVE_FORWARD, CommConstants.MSG_TO_ARDUINO);
				} else if (!hasObstacleOnLeft()){
					robotTurnLeft();
					commMgr.sendMsg(CommConstants.ROBOT_TURN_LEFT, CommConstants.MSG_TO_ARDUINO);
				} else {
					robotTurnRight();
					commMgr.sendMsg(CommConstants.ROBOT_TURN_RIGHT, CommConstants.MSG_TO_ARDUINO);
				}

				try{
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch(InterruptedException e){
					System.out.println("InterruptedException");
				}


				//for debugging
				// Scanner sc = new Scanner(System.in);
				// System.out.println("Press any key to continue...");
				// sc.nextLine();


				//set for ending condition

				//1. go back to start point
				if (sameGrid(expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER))&&leaveStart)
				endFlag = true;

				if (!sameGrid(expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER))){
					leaveStart = true;
				}

				knownMap.printExplorationProgress();
				knownMap.repaint();				

			}

			// if (limitReached){
			// 	//mark all the unexplored area as obstacle.
			// 	//use shortest path to go back to the start point
			// 	for (int i = 1; i < MapConstants.MAP_ROW-1; i++){
			// 		for (int j = 1; j < MapConstants.MAP_COL-1; j++){
			// 			if (!knownMap.getGrid(i, j).isExplored()){
			// 				knownMap.addObstacle(i, j);
			// 			}
			// 		}
			// 	}

			// 	ShortestPathAlgo s = new ShortestPathAlgo(knownMap, expRobot, expRobot.getPosition(), new MapGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER));
			// 	s.runShortestPath();

		}
	}


	private void updateSensorsReading(){
		//request sensor reading
		commMgr.sendMsg(CommConstants.REQUEST_SENSOR_READING, CommConstants.MSG_TO_ARDUINO);

		//get sensors reading
		int[] sensorReading = new int[5];
		for (int i = 0; i < 5; i++){
			sensorReading[i] = Integer.parseInt(commMgr.recvMsg())+1;
		}
		ArrayList<Sensor> sensors = expRobot.getSensors();
		int k = 0;
		for (Sensor s : sensors){
			int detectDirection = getDetectDirection(s.getDirection(), expRobot.getHeading());
			int xtemp, ytemp;
			MapGrid sensorCurPos = getSensorCurrentPosition(s);
			for (int i=1; i<=s.getRange();i++){
				switch (detectDirection){
					case 1:
						xtemp = sensorCurPos.getRow();
						ytemp = sensorCurPos.getCol()+i;

						if (realDetectCurrentGrid(xtemp, ytemp, i, sensorReading, k))
							i = s.getRange()+1;   //break the loop
						break;
					case 2:
						xtemp = sensorCurPos.getRow()-i;
						ytemp = sensorCurPos.getCol();


						if (realDetectCurrentGrid(xtemp, ytemp, i, sensorReading, k))
							i = s.getRange()+1;   //break the loop
						break;
					case 3:
						xtemp = sensorCurPos.getRow();
						ytemp = sensorCurPos.getCol()-i;


						if (realDetectCurrentGrid(xtemp, ytemp, i, sensorReading, k))
							i = s.getRange()+1;   //break the loop
						break;
					case 4:
						xtemp = sensorCurPos.getRow()+i;
						ytemp = sensorCurPos.getCol();


						if (realDetectCurrentGrid(xtemp, ytemp, i, sensorReading, k))
							i = s.getRange()+1;   //break the loop
						break;
				}
			}
			k++;
		}

	}


	private boolean realDetectCurrentGrid(int x, int y, int length, int[] sensorReading, int k){
		
		if (x<1||x>MapConstants.MAP_ROW-2||y<1||y>MapConstants.MAP_COL-2){
			return false;
		}

		knownMap.getGrid(x, y).setExplored(true);
		//System.out.println(trueGrid.isObstacle());
		if (length == sensorReading[k]){
			knownMap.addObstacle(x, y);
			return true;
		} else {
			return false;
		}		
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

	private boolean shouldCalibration(){
		MapGrid curPos = expRobot.getPosition();
		MapGrid frontGrid = null;
		MapGrid frontRightGrid = null;
		MapGrid frontLeftGrid = null;
		int curRow = curPos.getRow();
		int curCol = curPos.getCol();
		int curHeading = expRobot.getHeading();
		switch (curHeading){
			case 1:
				frontGrid = knownMap.getGrid(curRow, curCol+2);
				frontRightGrid = knownMap.getGrid(curRow-1, curCol+2);
				frontLeftGrid = knownMap.getGrid(curRow+1, curCol+2);
				break;
			case 2:
				frontGrid = knownMap.getGrid(curRow-2, curCol);
				frontRightGrid = knownMap.getGrid(curRow-2, curCol-1);
				frontLeftGrid = knownMap.getGrid(curRow-2, curCol+1);
				break;
			case 3:
				frontGrid = knownMap.getGrid(curRow, curCol-2);
				frontRightGrid = knownMap.getGrid(curRow+1, curCol-2);
				frontLeftGrid = knownMap.getGrid(curRow-1, curCol-2);
				break;
			case 4:
				frontGrid = knownMap.getGrid(curRow+2, curCol);
				frontRightGrid = knownMap.getGrid(curRow+2, curCol+1);
				frontLeftGrid = knownMap.getGrid(curRow+2, curCol-1);
				break;
		}
		// System.out.println(curPos.toString());
		// System.out.println(curHeading);
		// System.out.println(frontGrid.toString());
		// System.out.println(frontRightGrid.toString());
		// System.out.println(frontLeftGrid.toString());
		return frontGrid.isObstacle() && frontRightGrid.isObstacle() && frontLeftGrid.isObstacle();
	}

	

	private String generateMapDescriptor(){
		String mapDescriptor = "";

		//part1
		mapDescriptor += "11\n";
		for (int i=1;i<MapConstants.MAP_ROW-1;i++){
			for (int j=1; j<MapConstants.MAP_COL-1;j++){
				if (knownMap.getGrid(i, j).isExplored()){
					mapDescriptor += "1";
				}
				else {
					mapDescriptor += "0";
				}
			}
			mapDescriptor += "\n"; 
		}
		mapDescriptor += "11\n";

		String part1Hex = stringBinaryToHex(mapDescriptor);

		System.out.println();
		System.out.println("Map mapDescriptor:");
		System.out.println("Part 1");
		System.out.println(part1Hex);

		System.out.println(mapDescriptor);

		//part2
		mapDescriptor = "";
		for (int i=1;i<MapConstants.MAP_ROW-1;i++){
			for (int j=1; j<MapConstants.MAP_COL-1;j++){
				if (knownMap.getGrid(i, j).isExplored()){
					if (knownMap.getGrid(i, j).isObstacle()){
						mapDescriptor += "1";
					}
					else {
						mapDescriptor += "0";
					}
				}
			}
			mapDescriptor += "\n"; 
		}

		String part2Hex = stringBinaryToHex(mapDescriptor);
		System.out.println();
		System.out.println("Part 2");
		System.out.println(part2Hex);
		System.out.println(mapDescriptor);

		return part1Hex+part2Hex;
		
	}

	private String stringBinaryToHex(String binString){
		String hexString = "";
		int digitCount = 0;
		int sum = 0;
		String tempDigit = "";

		//padding to full byte length
		if (binString.length() % 8 != 0){
			for (int i = 0; i < 8-binString.length();i++){
				binString += "0";
			}
		}


		for (int i = binString.length()-1; i>=0; i--){
			if (binString.charAt(i) != '\n'){
				digitCount++;
				tempDigit = binString.charAt(i) + tempDigit;
				if (binString.charAt(i) == '1'){
					int temp = 1;
					for (int k = 0; k < digitCount-1; k++){
						temp *= 2;
					}
					sum += temp;
				}

			}

			if (digitCount == 4){

				if (sum < 10){
					hexString = Integer.toString(sum) + hexString;
				}
				else {
					switch (sum){
						case 10:
							hexString = "A" + hexString;
							break;
						case 11:
							hexString = "B" + hexString;
							break;
						case 12:
							hexString = "C" + hexString;
							break;
						case 13:
							hexString = "D" + hexString;
							break;
						case 14:
							hexString = "E" + hexString;
							break;
						case 15:
							hexString = "F" + hexString;
							break;
					}
				}
				digitCount = 0;
				sum = 0;
				tempDigit = "";
			}
		}
		return hexString;
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