package robot;

public class Sensor {

	private int range;
	private int direction; //same as robot: 1, forward;
						  //               2, right;
						  //               3, back;
						  //               4, left;
	private int xPos; // use 0, -1, 1
	private int yPos;

	public Sensor(int r, int d, int x, int y){
		this.range = r;
		this.direction = d;
		this.xPos = x;
		this.yPos = y;
	}

	public int getRange(){
		return this.range;
	}

	public int getDirection(){
		return this.direction;
	}

	public int getXPos(){
		return this.xPos;
	}

	public int getYPos(){
		return this.yPos;
	}
	

}
