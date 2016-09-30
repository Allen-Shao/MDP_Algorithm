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



		//initialize
		realRobot = new Robot(new MapGrid(2, 2), 1);
		realMap = new Map(realRobot);
		realMap.addBoarder();


		ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); //first parameter not used in real run;
		ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);





	}





}