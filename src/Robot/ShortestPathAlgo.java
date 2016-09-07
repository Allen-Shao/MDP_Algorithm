package robot;

import map.Map;
import map.MapGrid;
import map.MapConstants
import robot.Robot;

public class ShortestPathAlgo {
	private Map stpMap;     //shortest path map
	private Robot robot;


	private MapGrid start;
	private MapGrid goal;

	private ArrayList<MapGrid> opened = new ArrayList();
	private ArrayList<MapGrid> closed = new ArrayList();

	public ShortestPathAlgo(Map m, Robot r){
		this.stpMap = m;
		this.robot = r;

		this.start = stpMap.getGrid(MapConstants.START_X_CENTER, MapConstants.START_Y_CENTER);
		this.goal = stpMap.getGrid(MapConstants.GOAL_X_CENTER, MapConstants.GOAL_Y_CENTER);

	}

	public void runShortestPath(){
		
		if (sameGrid(robot.getPostion(), start)){
			//To be implemented
			return;
		}


		opened.add(this.start);

		while (!opened.isEmpty()){
			MapGrid current = opened[0];


			if (sameGrid(current, goal))
				//To be implemented
				return;

			opened.remove(current);
			closed.add(current);

			ArrayList<MapGrid> neighbours = findNeighbour(current);
			for (int i; i<neighbours.size();i++){
				if (!gridInTheList(neighbours[i], closed)){



				}
					


			}






		}

	}



	private ArrayList<MapGrid> findNeighbour(MapGrid cur){
		ArrayList<MapGrid> neighbours = new ArrayList();
		for (int i=-1; i<2; i++){
			for (int j=-1; j<2; j++){
				neighbours.add(map.getGrids[cur.getRow()+i][cur.getCol()+j]);
			}
		}

		neighbours.remove(cur);

		return neighbours;	
	}

	private boolean sameGrid(MapGrid a, MapGrid b){
		return (a.getCol == b.getCol) && (a.getRow == b.getCol);
	}

	private boolean gridInTheList(MapGrid a, ArrayList list){
		for (int i = 0; i<list.size(); i++){
			if (sameGrid(a, list[i]))
				return true;
		}
		return false;
	}

}
