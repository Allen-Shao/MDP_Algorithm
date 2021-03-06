package map;

public class MapGrid {
	private int xPos;
	private int yPos;

	private boolean explored;
	private boolean obstacle;
	private boolean visited;
	private boolean virtualWall;


	public MapGrid(int x, int y){
		this.xPos = x;
		this.yPos = y;

		explored = false;
		obstacle = false;
		virtualWall = false;
		visited = false;

	}

	public int getRow(){
		return this.xPos;
	}

	public int getCol(){
		return this.yPos;
	}

	public boolean isExplored(){
		return explored;
	}

	public boolean isObstacle(){
		return obstacle;
	}

	public boolean isVirtualWall(){
		return virtualWall;
	}

	public boolean isVisted(){
		return visited;
	}

	public void setRow(int x){
		this.xPos = x;
	}

	public void setCol(int y){
		this.yPos = y;
	}

	public void setExplored(boolean e){
		this.explored = e;
	}

	public void setVisited(boolean v){
		this.visited = v;
	}

	public void setObstacle(boolean o){
		this.obstacle = o;
	}

	public void setVirtualWall(boolean v){
		this.virtualWall = v;
	}


	public void reset(){
		explored = false;
		obstacle = false;
		virtualWall = false;
	}

	public String toString(){
		return "("+ Integer.toString(this.xPos) + ", " + Integer.toString(this.yPos) + ")";
	}


}
