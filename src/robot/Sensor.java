package robot;

public class Sensor {

	private int range;
	private int direction; //same as robot: 1, forward;
						  //               2, right;
						  //               3, back;
						  //               4, left;

	public Sensor(int r, int d){
		this.range = r;
		this.direction = d;
	}

	private int getRange(){
		return this.range;
	}

	private int getDirection(){
		return this.direction;
	}
	

}
