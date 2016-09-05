package map;

public final class MapConstants {


	//Number of map row and column, +2 to include the border wall
	public static final int MAP_COL = 17;
	public static final int MAP_ROW = 22;

	//Grid size 10 cm
	public static final int GRID_SIZE = 10;

	//Define start zone
	public static final int START_X_MIN = 1;
	public static final int START_X_MAX = 3;
	public static final int START_Y_MIN = 1;
	public static final int START_Y_MAX = 3;

	//Define goal zone
	public static final int GOAL_X_MIN = MAP_ROW - 4;
	public static final int GOAL_X_MAX = MAP_ROW - 2;
	public static final int GOAL_Y_MIN = MAP_COL - 4;
	public static final int GOAL_Y_MAX = MAP_COL - 2;
}