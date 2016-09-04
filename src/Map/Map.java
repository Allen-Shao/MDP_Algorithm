package map;

public class Map{

	protected MapGrid [][] grids = null;

	public Map(){
		grids = new MapGrid [Constants.MAP_ROW][Constants.MAP_COL];

		for (int i = 0; i < Constants.MAP_ROW; i++){
			for (int j = 0; j < Constants.MAP_COL; j++){
				grids[i][j] = new MapGrid(i, j);
			}
		}		
	}

	public Grid[][] getGrids(){
		return grids;
	}

	public void resetMap(){
		for (int i = 0; i < Constants.MAP_ROW; i++){
			for (int j = 0; j < Constants.MAP_COL; j++){
				grids[i][j].reset();
			}
		}	
	}

	public boolean isBorder(int x, int y){
		if ((x == 0) || (x == Constants.MAP_ROW-1) || (y == 0) || (y == Constants.MAP_COL-1))
			return true;
		else 
			return false;
	}

	public boolean isStartZone(int x, int y){
		if ((x >= Constants.START_X_MIN) || (x <= Constants.START_X_MAX) || (y >= Constants.START_Y_MIN) || (y <= Constants.START_Y_MAX))
			return true;
		else 
			return false;
	}

	public boolean isGoalZone(int x, int y){
		if ((x >= Constants.GOAL_X_MIN) || (x <= Constants.GOAL_X_MAX) || (y >= Constants.GOAL_Y_MIN) || (y <= Constants.GOAL_Y_MAX))
			return true;
		else 
			return false;
	}



}