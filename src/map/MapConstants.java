package map;

import java.awt.Color;

public final class MapConstants {


	//Number of map row and column, +2 to include the border wall
	public static final int MAP_COL = 17;
	public static final int MAP_ROW = 22;

	//Grid size 10 cm
	public static final int GRID_SIZE = 40;

	//Define start zone
	public static final int START_X_MIN = 1;
	public static final int START_X_MAX = 3;
	public static final int START_Y_MIN = 1;
	public static final int START_Y_MAX = 3;
	public static final int START_X_CENTER = 2;
	public static final int START_Y_CENTER = 2;

	//Define goal zone
	public static final int GOAL_X_MIN = MAP_ROW - 4;
	public static final int GOAL_X_MAX = MAP_ROW - 2;
	public static final int GOAL_Y_MIN = MAP_COL - 4;
	public static final int GOAL_Y_MAX = MAP_COL - 2;
	public static final int GOAL_X_CENTER = MAP_ROW - 3;
	public static final int GOAL_Y_CENTER = MAP_COL - 3;

	//colors for gui
	public static final Color C_BORDER = Color.BLACK;
	public static final Color C_BORDER_WARNING = new Color(255, 102, 153, 200);
	
	public static final Color C_GRID_LINE = Color.ORANGE;
	public static final int GRID_LINE_WEIGHT = 2;
	
	public static final Color C_START = Color.BLUE;
	public static final Color C_GOAL = Color.GREEN;
	
	public static final Color C_UNEXPLORED = Color.LIGHT_GRAY;
	public static final Color C_FREE = Color.WHITE;
	public static final Color C_OBSTACLE = Color.DARK_GRAY;
	public static final Color C_ROBOT = Color.RED;
	public static final Color C_ROBOT_DIR = new Color(0, 46, 155, 220);
}