/*
 * File: Skeleton.java
 * By: Joe Shacar
 * Date: 04/13/2020
 *
 * Skeleton is a subclass of Enemy. It has a constructor that customizes its
 * image. The isAvailMove method is overridden and defines the Skeleton
 * move set.
 *
 */
import javax.swing.*;
import java.util.ArrayList;

class Skeleton extends Enemy
{
	//Skeleton Constructor
    public Skeleton()
    {
		setType("Skeleton");
		entityCoordinate = new Coordinate(this.getRow(), this.getCol());
		setImage(new ImageIcon ("images/skeleton.png"));
    }
	
	public boolean isAvailMove(int indexRow, int indexColumn)
	{
		//Skeleton's move set = 1 diagonal tile
		int rowDiff = this.getRow() - indexRow;
		int columnDiff = this.getCol() - indexColumn;
		int rowDiffABS = Math.abs(rowDiff);
		int colDiffABS = Math.abs(columnDiff);
		int DiffSum = Math.abs(columnDiff) + Math.abs(rowDiff);
		if (colDiffABS == rowDiffABS && DiffSum == 2) return true;
		else return false;
	}
}