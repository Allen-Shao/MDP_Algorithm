package map;

import java.util.*;
import java.io.*;
import robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel{

	protected MapGrid [][] grids = null;

	private GUIGrid [][] guiGrids = null;

	private Robot mapRobot = null;

	public Map(Robot r){
		grids = new MapGrid [MapConstants.MAP_ROW][MapConstants.MAP_COL];

		mapRobot = r;

		for (int i = 0; i < MapConstants.MAP_ROW; i++){
			for (int j = 0; j < MapConstants.MAP_COL; j++){
				grids[i][j] = new MapGrid(i, j);
			}
		}		
	}

	public MapGrid getGrid(int i, int j){
		return grids[i][j];
	}

	public void setGrid(int i, int j, MapGrid newGrid){
		this.grids[i][j] = newGrid;
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


	public void addObstacle(int i, int j){
		this.grids[i][j].setObstacle(true);
		this.grids[i][j].setVirtualWall(false); //if it is set to obstacle, it is not a virtual wall

		//set virtual wall
		for (int m=-1; m<2; m++){
			for (int n=-1; n<2; n++){
				if ((i+m)>0 && (i+m)<MapConstants.MAP_ROW && (j+n)>0 && (j+n)<MapConstants.MAP_COL){
					if (grids[i+m][j+n].isObstacle() == false && grids[i+m][j+n].isVirtualWall() == false){
						// System.out.printf("%d %d\n", i+m, j+n);
						this.grids[i+m][j+n].setVirtualWall(true);
					}
				}
			}
		}

	}

	public void removeVirtualWall(){           //exclude border virtual wall
		for (int i = 2; i<MapConstants.MAP_ROW-2; i++){
			for (int j = 2; j<MapConstants.MAP_COL-2; j++){
				this.grids[i][j].setVirtualWall(false);
			}
		}
	}

	public void addBorder(){
		for (int i=0;i<MapConstants.MAP_ROW;i++){
			for (int j=0; j<MapConstants.MAP_COL;j++){
				if (isBorder(i,j)){
					addObstacle(i, j);
				}
			}
		}
	}


	public void loadMap(String filename){

		BufferedReader brStream;
		FileReader frStream;

		try {
			frStream = new FileReader(filename);
			brStream = new BufferedReader(frStream);

			int input;
			int i = MapConstants.MAP_ROW-1, j = 1;
			while (i >= 1){
				while ((input = brStream.read()) != 10 && (input != -1)){
					if (input == 49){
						addObstacle(i, j);
						// printMap();
					}
					//System.out.println(input);
					j++;
				}
				i--; j=1;
			}
		} catch (IOException e){
			System.out.println(e);
		}

		//add border
		addBorder();
	}

	public void paintComponent(Graphics g){

		// Calculate the map grids for rendering
		guiGrids = new GUIGrid[MapConstants.MAP_ROW][MapConstants.MAP_COL];
		for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++) {
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++) {
				guiGrids[mapRow][mapCol] = new GUIGrid(mapCol
						* MapConstants.GRID_SIZE, mapRow
						* MapConstants.GRID_SIZE, MapConstants.GRID_SIZE);
			}
		}

        // Paint the grids
        for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++)
		{
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++)
			{
				
				Color gridColor = null;
				
				// Determine what color to fill grid
				
				if(isStartZone(mapRow, mapCol))
					gridColor = MapConstants.C_START;
				else if(isGoalZone(mapRow, mapCol))
					gridColor = MapConstants.C_GOAL;
				else
				{	
					//if unexplored
					if (!grids[mapRow][mapCol].isExplored()){
						gridColor = MapConstants.C_UNEXPLORED;
					}
					else if(grids[mapRow][mapCol].isObstacle())
						gridColor = MapConstants.C_OBSTACLE;
					else
						gridColor = MapConstants.C_FREE;
				}
				
				g.setColor(gridColor);
				g.fillRect(guiGrids[mapRow][mapCol].gridX,
						guiGrids[mapRow][mapCol].gridY,
						guiGrids[mapRow][mapCol].gridSize,
						guiGrids[mapRow][mapCol].gridSize);
				
			}
		} // End outer for loop	

		//paint robot
		g.setColor(MapConstants.C_ROBOT);
		int r = mapRobot.getPosition().getRow()-1;
		int c = mapRobot.getPosition().getCol()-1;
		g.fillOval((c-1) * MapConstants.GRID_SIZE + 22,758 - (r * MapConstants.GRID_SIZE + 18),76,76);


		//paint direction
		g.setColor(MapConstants.C_ROBOT_DIR);
		int d = mapRobot.getHeading();
		switch (d) {
			case 4: 
				g.fillOval(c * MapConstants.GRID_SIZE + 12,758 -r * MapConstants.GRID_SIZE - 22,18,18);
				break;
			case 3:
				g.fillOval(c * MapConstants.GRID_SIZE - 22,758 - r * MapConstants.GRID_SIZE + 8 ,18,18);
				break;
			case 2:
				g.fillOval(c * MapConstants.GRID_SIZE + 12,758 - r * MapConstants.GRID_SIZE + 42,18,18);
				break;
			case 1:
				g.fillOval(c * MapConstants.GRID_SIZE + 42,758 - r * MapConstants.GRID_SIZE + 8,18,18);
				break;
		}
	}

	private class GUIGrid {
		public int borderX;
		public int borderY;
		public int borderSize;
		
		public int gridX;
		public int gridY;
		public int gridSize;
		
		public GUIGrid(int borderX, int borderY, int borderSize) {
			this.borderX = borderX;
			this.borderY = borderY;
			this.borderSize = borderSize;
			
			this.gridX = borderX + MapConstants.GRID_LINE_WEIGHT;
			this.gridY = 758 - (borderY - MapConstants.GRID_LINE_WEIGHT);
			this.gridSize = borderSize - (MapConstants.GRID_LINE_WEIGHT * 2);
		}
	}	





	//Print for debugging
	public void printMapWithVirtualWall(){
		for (int i=0;i<MapConstants.MAP_ROW;i++){
			for (int j=0; j<MapConstants.MAP_COL;j++){
				if (grids[i][j].isVirtualWall()){
					System.out.print("2");
				}else if (grids[i][j].isObstacle()){
					System.out.print("1");
				}
				else {
					System.out.print("0");
				}
			}
			System.out.println();
		}

		System.out.println();

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

		System.out.println();

	}

	public void printExplorationProgress(Robot r){
		for (int i=MapConstants.MAP_ROW-1;i>=0;i--){
			for (int j=0; j<MapConstants.MAP_COL;j++){
				if (r.getPosition().getRow()== i && r.getPosition().getCol() == j){
					switch(r.getHeading()){    					//print robot position
						case 1: System.out.print(">");break;
						case 2: System.out.print("v");break;
						case 3: System.out.print("<");break;
						case 4: System.out.print("^");break;
						default: System.out.printf("%d", r.getHeading());break;
					}						
				} else if (!grids[i][j].isExplored() && !isBorder(i, j)){
					System.out.print("x");						//unexplored area
				} else if (grids[i][j].isObstacle()){
					System.out.print("1");						//obstacle
				}
				else if (grids[i][j].isVirtualWall()){
					System.out.print("2");						//virtual wall
				}
				else {
					System.out.print("0");						//empty area(can go)
				}
			}
			System.out.println();
		}

		System.out.println();	

	}



}