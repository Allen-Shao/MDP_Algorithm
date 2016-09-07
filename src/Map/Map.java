package map;

import java.util.*;
import java.io.*;


public class Map{

	protected MapGrid [][] grids = null;

	public Map(){
		grids = new MapGrid [MapConstants.MAP_ROW][MapConstants.MAP_COL];

		for (int i = 0; i < MapConstants.MAP_ROW; i++){
			for (int j = 0; j < MapConstants.MAP_COL; j++){
				grids[i][j] = new MapGrid(i, j);
			}
		}		
	}

	public MapGrid[][] getGrid(int i, int j){
		return grids[i][j];
	}

	public void resetMap(){
		for (int i = 0; i < MapConstants.MAP_ROW; i++){
			for (int j = 0; j < MapConstants.MAP_COL; j++){
				grids[i][j].reset();
			}
		}	
	}

	public boolean isBorder(int x, int y){
		if ((x == 0) || (x == MapConstants.MAP_ROW-1) 
			|| (y == 0) || (y == MapConstants.MAP_COL-1))
			return true;
		else 
			return false;
	}

	public boolean isStartZone(int x, int y){
		if ((x >= MapConstants.START_X_MIN) || (x <= MapConstants.START_X_MAX) 
			|| (y >= MapConstants.START_Y_MIN) || (y <= MapConstants.START_Y_MAX))
			return true;
		else 
			return false;
	}

	public boolean isGoalZone(int x, int y){
		if ((x >= MapConstants.GOAL_X_MIN) || (x <= MapConstants.GOAL_X_MAX) 
			|| (y >= MapConstants.GOAL_Y_MIN) || (y <= MapConstants.GOAL_Y_MAX))
			return true;
		else 
			return false;
	}


	public void loadMap(String filename){

		BufferedReader brStream;
		FileReader frStream;

		try {
			frStream = new FileReader(filename);
			brStream = new BufferedReader(frStream);

			int input;
			int i = 1, j = 1;
			while (i <= MapConstants.MAP_ROW - 1){
				while ((input = brStream.read()) != 10 && (input != -1)){
					if (input == 49){
						grids[i][j].setObstacle(true);
					}
					System.out.println(input);
					j++;
				}
				i++; j=1;
			}
		} catch (IOException e){
			System.out.println(e);
		}

		//add border
		for (int i=0;i<MapConstants.MAP_ROW;i++){
			for (int j=0; j<MapConstants.MAP_COL;j++){
				if (isBorder(i,j)){
					grids[i][j].setObstacle(true);
				}
			}
		}

	}

	public void printMap(){
		for (int i=0;i<MapConstants.MAP_ROW;i++){
			for (int j=0; j<MapConstants.MAP_COL;j++){
				if (grids[i][j].isObstacle()){
					System.out.print("1");
				}
				else {
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}



}