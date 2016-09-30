package leaderboard;

import map.*;
import robot.*;
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
		commMgr.setConnection(10000);



		//initialize robot
		realRobot = new Robot(new MapGrid(2, 2), 1);

		//front sensor
		Sensor s1 = new Sensor(3, 1, 0, 1);
		Sensor s4 = new Sensor(3, 1, -1, 1);
		Sensor s5 = new Sensor(3, 1, 1, 1);
		//left sensor
		Sensor s2 = new Sensor(3, 2, -1, 1);
		//right sensor
		Sensor s3 = new Sensor(3, 4, 1, 1);

		mdpRobot.addSensor(s1);
		mdpRobot.addSensor(s2);
		mdpRobot.addSensor(s3);
		mdpRobot.addSensor(s4);
		mdpRobot.addSensor(s5);

		//initialize map
		realMap = new Map(realRobot);
		realMap.addBoarder();


		ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); //first parameter not used in real run;
		ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);





	}





}