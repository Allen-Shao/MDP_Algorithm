package simulator;

import javax.swing.*;

import java.util.*;
import java.io.*;
import map.*;
import robot.*;

public class Simulator {

	private static JFrame appFrame = null;

	private static JPanel mainCards = null;
	private static JPanel buttonCards = null;

	private static int mapXLength;
	private static int mapYLength;

	private static JPanel robotConfig = null;

	private static JPanel robotMap = null;

	private static JPanel mainButtons = null;

	private static JPanel robotMapButtons = null;

	private static Map map = null;

	private static Robot smartRobot = null;

	private static int startPosRow = 2;
	private static int startPosCol = 2;

	private static int startDir = 1;

	//This is non-gui version for testing

	public static void main(String[] args){
		map = new Map();

		map.loadMap("map.txt");

		map.printMap();

		


	}








	// public static void main(String[] args) {
		

		
	// 	// Initialize robot
	// 	if(smartRobot == null) {
	// 		smartRobot = new Robot();  //!! parameters to be added
	// 	}
		
	// 	// --------------------------------------------------------------------
	// 	// Everything below is just for the layout
		
	// 	// Calculate map width & height based on grid size
	// 	mapXLength = MapConstants.MAP_COL * MapConstants.GRID_SIZE;
	// 	mapYLength = MapConstants.MAP_ROW * MapConstants.GRID_SIZE;
		
	// 	// Main frame for displaying everything
	// 	appFrame = new JFrame();
	// 	appFrame.setTitle("MDP Simulator");
	// 	appFrame.setSize(new Dimension(886, 771));
	// 	appFrame.setResizable(false);

	// 	mainCards = new JPanel(new CardLayout());
	// 	buttonsCards = new JPanel(new CardLayout());









}