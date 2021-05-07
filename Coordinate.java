/*
 * File: Coordinate.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * The Coordinate class defines a custom object type to make it easier to
 * reference positions on the playing field. A coordinate is composed of two
 * integers representing the row and column. 
 *
 */
class Coordinate
{
	private int row;
	private int col;
	
	//Constructors
	//zero-arg creates [0, 0] coorindate
	public Coordinate ()
	{
		this.row = 0;
		this.col = 0;
	}
	//two-arg takes two ints for row and column
	public Coordinate (int r, int c)
	{
		this.row = r;
		this.col = c;
	}
	
	//Setters and Getters
	public void setCoord(int r, int c)
	{
		this.row = r;
		this.col = c;
	}
	public void setCoordRow(int r){this.row = r;}
	public void setCoordCol(int c){this.col = c;}
	public int getCoordRow(){return this.row;}
	public int getCoordCol(){return this.col;}
	
	//isEqual determines if two coords are identical
	public boolean isEqual (Coordinate c)
	{
		String a = this.row + ", " + this.col;
		String b = c.row + ", " + c.col;
		if (a.equals(b)) return true;
		else return false;
	}
	//toString method
	public String coordToString()
	{
		String str = "[" + this.row + "," + this.col + "]";
		return str;
	}
}