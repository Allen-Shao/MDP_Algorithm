package leaderboard;

public class CommConstants{

	//headers
	public static final String MSG_TO_ANDROID = "a";
	public static final String MSG_TO_ARDUINO = "h";

	//Messages
	public static final String ROBOT_MOVE_FORWARD = "w";
	public static final String ROBOT_TURN_LEFT = "a";
	public static final String ROBOT_TURN_RIGHT = "d";
	public static final String REQUEST_SENSOR_READING = "r";
	public static final String ROBOT_FRONT_CALIBRATION = "c";
	public static final String ROBOT_RIGHT_CALIBRATION = "x";
	public static final String ROBOT_LEFT_CALIBRATION = "y";

	public static final String ROBOT_FASTESTPATH = "f";

	public static final int COMM_DELAY_TIME = 100;
	//sensor order
	//1.front(left)
	//2.front(middle)
	//3.front(right)
	//4.left
	//5.right

}