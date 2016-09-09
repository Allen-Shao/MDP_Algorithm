package robot;

import map.Map;
import map.MapGrid;
import map.MapConstants
import robot.Robot;

import java.util.*;

public class ShortestPathAlgo{
	private Map stpMap;     //shortest path map
	private Robot robot;


	private MapGrid start;
	private MapGrid goal;

	private ArrayList<MapGrid> opened = new ArrayList<MapGrid>();
	private ArrayList<MapGrid> closed = new ArrayList<MapGrid>();
	private HashMap<MapGrid, MapGrid> parent = new HashMap<MapGrid,MapGrid>();

	private double gscore[][];

	public ShortestPathAlgo(Map m, Robot r){
		this.stpMap = m;
		this.robot = r;

		this.start = stpMap.getGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER);
		this.goal = stpMap.getGrid(MapConstants.GOAL_X_CENTER, MapConstants.GOAL_Y_CENTER);
	}

	private void runShortestPath(){
		
		if (sameGrid(robot.getPostion(), start)){
			//To be implemented
			System.out.println("The robot is not in the start zone!");
			return;
		}

		//initialize gscore
		for (int i = 0; i < MapConstants.Map_Row;i++){
			for (int j = 0; j < MapConstants.Map_Col;j++){
				if (stpMap.getGrid(i, j).isObstacle() || stpMap.getGrid(i, j).isVirtualWall() 
					|| stpMap.isBorder(i, j)){
					gscore[i][j] = RobotConstants.INIFINITY;
				}
				else {
					gscore[i][j] = - RobotConstants.INIFINITY;
				}
			}
		}
		//set gscore for start point
		gscore[start.getRow()][start.getCol()] = 0;
		//add the start point to opened set
		opened.add(this.start);

		//start A* search algorithm
		while (!opened.isEmpty()){

			//evaluate the grid in the opened set with lowest cost;
			MapGrid current = findMinimumCost(opened, gscore, goal);

			opened.remove(current);
			closed.add(current);

			//check if goal is reached
			if (closed.contains(stpMap.getGrid(goal.getRow(), goal.getCol()))){
				System.out.println("Shortest Path found.");
				//To be implemented
				
				return;
			}

			//find neighbours
			ArrayList<MapGrid> neighbours = findNeighbour(current);

			for (int i; i<neighbours.size();i++){
				curNeighbour = neighbours[i];
				if (!closed.contains(curNeighbour)){  //the neighbour grid has not been evaluated
					if (!opened.contains(curNeighbour)){
						parent.put(curNeighbour, current);
						gscore[curNeighbour.getRow()][curNeighbour.getCol()] = 
							gscore [current.getRow()][current.getCol()] 
							+ calculateGscore(current, curNeighbour, robot.getHeading());
						opened.add(curNeighbour);
					}
					else {
						double currentGscore = gscore[curNeighbour.getRow()][curNeighbour.getCol()];
						double newGscore = gscore[current.getRow()][current.getCol()] 
							+ calculateGscore(current, curNeighbour, robot.getHeading());
						if (newGscore < currentGscore){
							gscore[curNeighbour.getRow()][curNeighbour.getCol()] = newGscore;
							parent.put(curNeighbour, current);
						}

					}
				}
					
			}
		}
		System.out.println("Path NOT Found!");
		return;
	}

	private MapGrid findMinimumCost(ArrayList<MapGrid> list, double[][] gscore, MapGrid goal){
		int size = lis.size();
		double min = RobotConstants.INIFINITY;
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

		if (goal.getCol()-currerntGrid.getCol != 0 
			&& goal.getRow()-currentGrid.getRow()){
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
				break;
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
				break;
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
				break;
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
				break;
		}
	}


	private ArrayList<MapGrid> findNeighbour(MapGrid cur){
		ArrayList<MapGrid> neighbours = new ArrayList();
		MapGrid temp;

		temp = map.getGrids[cur.getRow()+1][cur.getCol()]
		if (!temp.isObstacle() || !temp.isVirtualWall()){
			neighbours.add(temp);
		}

		temp = map.getGrids[cur.getRow()-1][cur.getCol()]
		if (!temp.isObstacle() || !temp.isVirtualWall()){
			neighbours.add(temp);
		}

		temp = map.getGrids[cur.getRow()][cur.getCol()+1]
		if (!temp.isObstacle() || !temp.isVirtualWall()){
			neighbours.add(temp);
		}

		temp = map.getGrids[cur.getRow()][cur.getCol()-1]
		if (!temp.isObstacle() || !temp.isVirtualWall()){
			neighbours.add(temp);
		}

		return neighbours;	
	}

	private boolean sameGrid(MapGrid a, MapGrid b){
		return (a.getCol == b.getCol) && (a.getRow == b.getCol);
	}

}
