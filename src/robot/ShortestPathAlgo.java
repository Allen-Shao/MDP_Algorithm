package robot;

import map.Map;
import map.MapGrid;
import map.MapConstants;
import robot.Robot;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ShortestPathAlgo{
	private Map stpMap;     //shortest path map
	private Robot stpRobot;

	private MapGrid start;
	private MapGrid goal;

	private ArrayList<MapGrid> opened = new ArrayList<MapGrid>();
	private ArrayList<MapGrid> closed = new ArrayList<MapGrid>();
	private HashMap<MapGrid, MapGrid> parent = new HashMap<MapGrid,MapGrid>();

	private double[][] gscore = new double[MapConstants.MAP_ROW][MapConstants.MAP_COL];

	private Stack<MapGrid> path;

	public ShortestPathAlgo(Map m, Robot r){
		this.stpMap = m;
		this.stpRobot = r;

		this.start = stpMap.getGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER);
		this.goal = stpMap.getGrid(MapConstants.GOAL_X_CENTER, MapConstants.GOAL_Y_CENTER);
	}

	public ShortestPathAlgo(Map m, Robot r, MapGrid start, MapGrid goal){
		this.stpMap = m;
		this.stpRobot = r;
		this.start = start;
		this.goal = goal;
	}


	public Stack<MapGrid> runShortestPath(){
		
		if (!sameGrid(stpRobot.getPosition(), start)){
			System.out.println("The robot is not in the start zone!");
			// System.out.printf("%d %d", stpRobot.getPosition().getRow(), stpRobot.getPosition().getCol());
			// System.out.printf("%d %d", start.getRow(), start.getCol());
			return null;
		}

		stpMap.printMapWithVirtualWall();
		System.out.printf("%d %d", goal.getRow(), goal.getCol());
		//initialize gscore
		for (int i = 0; i < MapConstants.MAP_ROW;i++){
			for (int j = 0; j < MapConstants.MAP_COL;j++){
				if (stpMap.getGrid(i, j).isObstacle() || stpMap.getGrid(i, j).isVirtualWall() 
					|| stpMap.isBorder(i, j)){
					gscore[i][j] = RobotConstants.INFINITY;
				}
				else {
					gscore[i][j] = - RobotConstants.INFINITY;
				}
			}
		}
		// for (int i = 0; i < MapConstants.MAP_ROW;i++){
		// 	for (int j = 0; j < MapConstants.MAP_COL;j++){
		// 		System.out.printf("%6.1f ", gscore[i][j]);
		// 	}
		// 	System.out.println();
		// }			

		//set gscore for start point
		gscore[start.getRow()][start.getCol()] = 0;
		//add the start point to opened set
		opened.add(this.start);

		//start A* search algorithm
		do {

			//evaluate the grid in the opened set with lowest cost;
			MapGrid current = findMinimumCost(opened, gscore, goal);


			//check if goal is reached
			if (closed.contains(stpMap.getGrid(goal.getRow(), goal.getCol()))){
				System.out.println("Shortest Path found.");
				//To be implemented
				// for (int i = 0; i < MapConstants.MAP_ROW;i++){
				// 	for (int j = 0; j < MapConstants.MAP_COL;j++){
				// 		System.out.printf("%6.1f ", gscore[i][j]);
				// 	}
				// 	System.out.println();
				// }				
				path = generatePath(goal);

				moveRobot();

				return generatePath(goal);  //get the path towards goal
			}

			opened.remove(current);
			closed.add(current);

			//find neighbours
			ArrayList<MapGrid> neighbours = findNeighbour(current);
			// System.out.println(neighbours.size());

			for (int i=0; i<neighbours.size();i++){
				MapGrid curNeighbour = neighbours.get(i);
				if (!closed.contains(curNeighbour)){  //the neighbour grid has not been evaluated
					if (!opened.contains(curNeighbour)){
						parent.put(curNeighbour, current);
						gscore[curNeighbour.getRow()][curNeighbour.getCol()] = 
							gscore[current.getRow()][current.getCol()] 
							+ calculateGscore(current, curNeighbour, stpRobot.getHeading());
						opened.add(curNeighbour);
					}
					else {
						double currentGscore = gscore[curNeighbour.getRow()][curNeighbour.getCol()];
						double newGscore = currentGscore 
							+ calculateGscore(current, curNeighbour, stpRobot.getHeading());
						if (newGscore < currentGscore){
							gscore[curNeighbour.getRow()][curNeighbour.getCol()] = newGscore;
							parent.put(curNeighbour, current);
						}

					}
				}
					
			}
		} while(!opened.isEmpty());
		if (closed.contains(stpMap.getGrid(goal.getRow(), goal.getCol()))){
				System.out.println("Shortest Path found.");
							
				path = generatePath(goal);
				//printPath(path);

				moveRobot();

				
				return generatePath(goal);  //get the path towards goal
		}
		System.out.println("Path NOT Found!");
		// for (int i = 0; i < MapConstants.MAP_ROW;i++){
		// 	for (int j = 0; j < MapConstants.MAP_COL;j++){
		// 		System.out.printf("%6.1f ", gscore[i][j]);
		// 	}
		// 	System.out.println();
		// }
		//printPath(generatePath(goal));
		//System.out.println(closed.toString());
		//System.out.println(closed.contains(stpMap.getGrid(goal.getRow(), goal.getCol())));
		return null;
	}

	private MapGrid findMinimumCost(ArrayList<MapGrid> list, double[][] gscore, MapGrid goal){
		int size = list.size();
		double min = RobotConstants.INFINITY;
		MapGrid result = null;

		for (int i = size - 1; i >= 0; i--){
			MapGrid tempGrid = list.get(i);
			double tempCost =  gscore[tempGrid.getRow()][tempGrid.getCol()] 
							+ heuristicFunction(tempGrid, goal);   //calculate the fscore = gscore + hscore
			if (tempCost < min){
				min = tempCost;
				result = tempGrid;
			}
		}
		return result;
	}

	//!!!!!*currently use this heuristic function, may not be very optimal!!!!
	private double heuristicFunction(MapGrid currentGrid, MapGrid goal){
		double move = (Math.abs(goal.getRow() - currentGrid.getRow()) 
						+ Math.abs(goal.getCol() - currentGrid.getCol())) 
						* RobotConstants.MOVE_COST;

		double turn = 0;

		//when move = 0(arrive goal)
		if (move == 0){
			return 0.0;
		}

		if (goal.getCol()-currentGrid.getCol() != 0 
			&& goal.getRow()-currentGrid.getRow() != 0){
			turn = RobotConstants.TURN_COST; //turning once;
		}
		return (move + turn);
	}

	private double calculateGscore(MapGrid cur, MapGrid next, int heading){
		double move = RobotConstants.MOVE_COST; //always one step
		double turn = getTurnTimes(cur, next, heading) * RobotConstants.TURN_COST;

		return (move + turn);
	}

	private int getTurnTimes(MapGrid cur, MapGrid next, int heading){
		switch (heading) {
			case 1: //current direction to right
				if (next.getRow() == cur.getRow()){
					if (next.getRow() >= cur.getRow()){
						return 0; // cur -> next no need to turn
					} else {
						return 2;  // next cur->   need to turn twice
					}
				}
				else {
					return 1; // next
							  // cur ->         turn once
				}				
				// break;
			case 2: //current direction to down
				if (next.getCol() == cur.getCol()){
					if (next.getCol() <= cur.getCol()){
						return 0; // cur
								  // next   no need to turn
					} else {
						return 2;  // next   need to turn twice
					}			   // cur
				}
				else {
					return 1; // next cur   need to turn once
				}			  				
				// break;
			case 3: //current direction to left
				if (next.getRow() == cur.getRow()){
					if (next.getRow() <= cur.getRow()){
						return 0; // next <- cur no need to turn
					} else {
						return 2;  // <-cur next  need to turn twice
					}
				}
				else {
					return 1; // next
							  // <- cur         turn once
				}				
				// break;
			case 4://current direction to up
				if (next.getCol() == cur.getCol()){
					if (next.getCol() >= cur.getCol()){
						return 0; // next
								  // cur    no need to turn
					} else {
						return 2;  // cur   need to turn twice
					}			   // next
				}
				else {
					return 1; // next cur   need to turn once
				}			  				
				// break;
		}

		return 0;
	}

	private Stack<MapGrid> generatePath(MapGrid goal){
		Stack<MapGrid> path = new Stack<MapGrid>();
		MapGrid current = stpMap.getGrid(goal.getRow(), goal.getCol());
		while (current != null){
			path.push(current);
			current = parent.get(current);
		}
		return path;
	}

	private void moveRobot(){
		Stack<MapGrid> movePath = path;
		stpRobot.setPosition(start);
		while (!movePath.isEmpty()){
			System.out.println(stpRobot.getPosition().toString());
			MapGrid nextMove = movePath.pop();
			System.out.println(nextMove.toString());
			stpMap.repaint();

			switch(stpRobot.getHeading()){
				case 1:
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()+1){
						robotMoveForward();
					}
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()+1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					} 
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()-1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()-1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					break;
				case 2:
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()-1){
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()+1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()-1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()+1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					break;
				case 3:
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()-1){
						robotMoveForward();
					}
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()-1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()+1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()+1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					break;
				case 4:
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()+1){
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()-1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getRow() == stpRobot.getPosition().getRow() && nextMove.getCol() == stpRobot.getPosition().getCol()+1){
						robotTurnRight();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					if (nextMove.getCol() == stpRobot.getPosition().getCol() && nextMove.getRow() == stpRobot.getPosition().getRow()-1){
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotTurnLeft();
						try{
							TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
						} catch(InterruptedException e){
							System.out.println("InterruptedException");
						}
						stpMap.repaint();
						robotMoveForward();
					}
					break;
				default: break;
			}

			try{
				TimeUnit.MILLISECONDS.sleep(1000/stpRobot.getSpeed());
			} catch(InterruptedException e){
				System.out.println("InterruptedException");
			}

		}
	}

	private void robotMoveForward(){
		MapGrid curPos = stpRobot.getPosition();
		switch (stpRobot.getHeading()){
			case 1:
				stpRobot.setPosition(stpMap.getGrid(curPos.getRow(), curPos.getCol()+1));
				break;
			case 2:
				stpRobot.setPosition(stpMap.getGrid(curPos.getRow()-1, curPos.getCol()));
				break;
			case 3:
				stpRobot.setPosition(stpMap.getGrid(curPos.getRow(), curPos.getCol()-1));
				break;
			case 4:
				stpRobot.setPosition(stpMap.getGrid(curPos.getRow()+1, curPos.getCol()));
				break;
		}
	}

	private void robotTurnLeft(){
		int newHeading = stpRobot.getHeading()-1;
		if (newHeading == 0)
			newHeading = 4;
		stpRobot.setHeading(newHeading);		
	}

	private void robotTurnRight(){
		int newHeading = (stpRobot.getHeading()+1)%4;
		if (newHeading == 0)
			newHeading = 4;
		stpRobot.setHeading(newHeading);		
	}


	private ArrayList<MapGrid> findNeighbour(MapGrid cur){
		ArrayList<MapGrid> neighbours = new ArrayList<MapGrid>();
		ArrayList<MapGrid> tempList = new ArrayList<MapGrid>();

		if (cur.getCol()+1 < MapConstants.MAP_COL)
			tempList.add(stpMap.getGrid(cur.getRow(), cur.getCol()+1));

		if (cur.getRow()-1 > 0)
			tempList.add(stpMap.getGrid(cur.getRow()-1, cur.getCol()));

		if (cur.getCol()-1 > 0)
			tempList.add(stpMap.getGrid(cur.getRow(), cur.getCol()-1));

		if (cur.getRow()+1 < MapConstants.MAP_ROW)
			tempList.add(stpMap.getGrid(cur.getRow()+1, cur.getCol()));

		for (int i=0; i<tempList.size(); i++){
			MapGrid temp = tempList.get(i);
			if (!temp.isObstacle() && !temp.isVirtualWall()){
				neighbours.add(temp);
			}
		}

		return neighbours;	
	}

	private boolean sameGrid(MapGrid a, MapGrid b){
		return (a.getCol() == b.getCol()) && (a.getRow() == b.getRow());
	}

	public void printPath(Stack<MapGrid> path){
		while (!path.isEmpty()){
			System.out.print(path.pop().toString() + " ");
		}
		System.out.println();
	}

}
