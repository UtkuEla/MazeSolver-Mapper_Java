package de.tuhh.diss.lab5;

public class Map {

	// Map class is being used for recording the moves done by robot. So that, the robot can make smarter decisions at the intersections and turns. 
	
	int x=3 ;
	int y=3;
	int orientation=0;
	int[][] map = new int[7][7] ; 
	// The initial map is a 7x7 zeros matrix and the starting point is always at the center of the map.
	// So that, the map can contain every possible 4x4 maze in each direction.  

	public void mapWalker(String direction) { 
	// mapWalker method marks the squares that the robot passed, also it counts how many times the robot passed through each square.

		if (direction == "UP") {
			map[x-1][y] += 1;
			x= x-1;
		}
		else if (direction == "DOWN") {
			map[x+1][y] += 1;
			x=x+1;
		}
		else if (direction == "RIGHT") {
			map[x][y+1] += 1;
			y=y+1;
		}
		else if (direction == "LEFT") {
			map[x][y-1] += 1;
			y=y-1;
		}

	}

	public int mapCheck(int row, int column) {
		return map[row][column];
	}

	public void mapGyro(int turns) {
		// mapGyro method saves the current orientation of the robot. So that, the robot can map the maze in the correct directions regardless of initial orientation.
		
		orientation = orientation + turns; 

		if (orientation == 360) {
			orientation = 0;
		}
		else if (orientation == 270) {
			orientation = -90;
		}
		else if (orientation == -180) {
			orientation = 180;
		}
		else if (orientation == -270) {
			orientation = 90;
		}

	}


}






