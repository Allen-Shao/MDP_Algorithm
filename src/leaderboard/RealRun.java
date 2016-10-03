package leaderboard;

import map.Map;
import map.MapGrid;
import map.MapConstants;
import robot.Robot;
import robot.RobotConstants;
import robot.Sensor;
import robot.ShortestPathAlgo;
import robot.ExploreAlgo;
import java.util.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class RealRun extends JFrame{

	private static Robot realRobot;

	private static Map realMap;

	private static CommMgr commMgr = CommMgr.getCommMgr();

	public static void main(String args[]){


		//set connection
		commMgr.setConnection();



		//initialize robot
		realRobot = new Robot(new MapGrid(2, 2), 1);

		//front sensor
		Sensor s1 = new Sensor(3, 1, 0, 1);
		Sensor s4 = new Sensor(3, 1, -1, 1);
		Sensor s5 = new Sensor(3, 1, 1, 1);
		//right sensor
		Sensor s2 = new Sensor(3, 2, -1, 1);
		//left sensor
		Sensor s3 = new Sensor(5, 4, 1, 1);

		realRobot.addSensor(s5);
		realRobot.addSensor(s1);
		realRobot.addSensor(s4);
		realRobot.addSensor(s3);
		realRobot.addSensor(s2);

		//initialize map
		realMap = new Map(realRobot);
		realMap.addBorder();


		ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); 
		ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);

		e.runRealExploration();





	}





}