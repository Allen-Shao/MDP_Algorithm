package robot;

public class Sensor {

	private int range;
	private int direction; //same as robot: 1, forward;
						  //               2, right;
						  //               3, back;
						  //               4, left;
	private int row; // use 0, -1, 1 to represent 
	private int col; // relative position of the centre of the robot

	public Sensor(int r, int d, int x, int y){
		this.range = r;
		this.direction = d;
		this.row = x;
		this.col = y;
	}

	public int getRange(){
		return this.range;
	}

	public int getDirection(){
		return this.direction;
	}

	public int getRow(){
		return this.row;
	}

	public int getCol(){
		return this.col;
	}
	

}
