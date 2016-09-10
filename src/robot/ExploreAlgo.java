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

		

	}


}