/*
 * File: Entity.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Entity is the superclass to Player and Enemy (and by extension the enemy
 * classes Slime, Skeleton, and Wolf). This class gives each character on
 * the play field a position expressed in row/column and cooridate format.
 * Entities also have a running list of available moves stored in a
 * ArrayList of Coordinate objects.
 *
 */
import javax.swing.*;
import java.util.ArrayList;

abstract class Entity
{
    private int posRow;
    private int posCol;
	private ImageIcon image;
	protected Coordinate entityCoordinate;
	protected ArrayList<Coordinate> availableMoves =
		new ArrayList<Coordinate>();

	//Row/Column Setters and Getters
    protected void setRow(int r){posRow = r;}
    protected void setCol(int c){posCol = c;}
	
	protected int getRow(){return posRow;}
    protected int getCol(){return posCol;}
	
	//Coorindate Setters and Getters
	protected void setCoord(int r, int c)
	{
		entityCoordinate.setCoordRow(r);
		entityCoordinate.setCoordCol(c);
	}
	protected Coordinate getCoord(){return entityCoordinate;}
	
	//Image Setters and Getters
	protected void setImage(ImageIcon i){image = i;}
	protected ImageIcon getImage(){return image;}
	
	//availableMoves Methods
	//(add, remove, get, and clear)
	protected void addAvailableMove(int r, int c)
	{
		availableMoves.add (new Coordinate (r, c));
	}
	
	protected void remAvailableMove(int r, int c)
	{
		Coordinate coord = new Coordinate (r, c);
		Coordinate coordToRemove = null;
		for (int i = 0; i < availableMoves.size(); i++)
		{
			if(coord.isEqual(availableMoves.get(i)))
			{
				coordToRemove = availableMoves.get(i);
			}
		}
		availableMoves.remove(coordToRemove);
	}
	
	protected ArrayList<Coordinate> getAvailableMoves()
    {
		return availableMoves;
    }
	
	protected void clearAvailableMoves()
    {
		availableMoves.clear();
    }
	
	//toString Methods
	protected String getAvailableMovesString()
    {
		String str = "";
		for(int i = 0; i < availableMoves.size(); i++)
		{   
			str += availableMoves.get(i).coordToString() + " ";
		}
		return str;
    }
	
	abstract protected boolean isAvailMove(int row, int column);
}